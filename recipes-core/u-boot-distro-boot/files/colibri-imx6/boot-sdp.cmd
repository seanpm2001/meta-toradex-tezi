setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480M@60D video=HDMI-A-1:640x480M@60D rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall

# Execute previously loaded FIT image
bootm 0x12100000
