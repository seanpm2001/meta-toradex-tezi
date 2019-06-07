setenv bootargs "quiet console=ttyLP1,115200 earlycon=lpuart32,0x5a070000,115200,115200 video=HDMI-A-1:640x480 video=imxdpufb5:off video=imxdpufb6:off video=imxdpufb7:off rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}" 

# Set address outside the range where FIT Image is extracted
setenv ramdisk_addr_r 0x94400000 

# Reenable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Load hdmi firmware
run loadhdp; hdp load ${hdp_addr}

# Load FIT Image and boot
fatload mmc ${mmcdev}:${mmcpart} ${ramdisk_addr_r} tezi.itb
bootm ${ramdisk_addr_r}

# TODO: modify when distroboot is set by default
# Load FIT image from location as detected by distroboot 
# load ${devtype} ${devnum}:${distro_bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb

