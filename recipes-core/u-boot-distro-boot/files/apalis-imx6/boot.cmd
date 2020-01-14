setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480D video=HDMI-A-1:640x480-16@60D video=LVDS-1:d video=VGA-1:d rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall ${teziargs}

# Reenable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Load FIT image from location as detected by distroboot
load ${devtype} ${devnum}:${distro_bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb

bootm ${ramdisk_addr_r}
