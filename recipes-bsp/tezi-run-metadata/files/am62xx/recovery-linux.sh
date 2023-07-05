#!/bin/bash

DFU_UTIL=$(which dfu-util 2>/dev/null)
if [ -z "$DFU_UTIL" ] || ! $DFU_UTIL -w -V &>/dev/null
then
	echo "Install or update dfu-util from your distro should the provided one not work with your distro"
	DFU_UTIL=recovery/dfu-util
fi

# USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A53 SPL
VID_PID_ROM="0451:6165"
VID_PID_R5="0451:6165"
VID_PID_A53="0451:6165"

# tiboot3.bin depends on the SoC type, GP or HS-FS
TIBOOT3_GP_BIN=tiboot3-am62x-gp-evm.bin-dfu
TIBOOT3_HSFS_BIN=tiboot3-am62x-hs-fs-evm.bin-dfu

wait_usb_device()
{
	VID_PID=$1

	while ! lsusb -d $VID_PID
	do
		sleep 1
	done
}

# set TIBOOT3_BIN variable according to the SoC type (GP or HF-FS)
select_tiboot3_bin()
{
	local soc_type
	local tmp_dir
	local soc_id_bin

	wait_usb_device $VID_PID_ROM

	tmp_dir=$(mktemp -d) || exit 1
	soc_id_bin=$tmp_dir/SocId.bin
	sudo $DFU_UTIL -R -a SocId --device $VID_PID_ROM -U $soc_id_bin

	soc_type=$(dd if=$soc_id_bin bs=1 count=4 skip=20 2>/dev/null)

	if [[ "$soc_type" == *"GP"* ]]
	then
		TIBOOT3_BIN=$TIBOOT3_GP_BIN
	else
		TIBOOT3_BIN=$TIBOOT3_HSFS_BIN
	fi

	rm -rf $tmp_dir
}

# select correct tiboot3.bin depending on SoC type
select_tiboot3_bin

# load boot binaries, boot script and tezi fitimage to RAM and boot U-Boot
wait_usb_device $VID_PID_ROM
sudo $DFU_UTIL -w -R -a bootloader --device $VID_PID_ROM -D $TIBOOT3_BIN
wait_usb_device $VID_PID_R5
sudo $DFU_UTIL -w -R -a tispl.bin --device $VID_PID_R5 -D tispl.bin
wait_usb_device $VID_PID_A53
sudo $DFU_UTIL -w -a u-boot.img --device $VID_PID_A53 -D u-boot.img-recoverytezi
sudo $DFU_UTIL -w -a ramdisk_addr_r --device $VID_PID_A53 -D tezi.itb
sudo $DFU_UTIL -w -a loadaddr --device $VID_PID_A53 -D overlays.txt
sudo $DFU_UTIL -w -R -a scriptaddr --device $VID_PID_A53 -D boot-tezi.scr

exit 0
