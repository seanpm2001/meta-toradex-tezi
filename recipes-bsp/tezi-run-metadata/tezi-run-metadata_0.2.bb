DESCRIPTION = "Toradex Easy Installer Metadata for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS:am62xx = "dfu-util-native patchelf-native"
MCDEPENDS = ""
MCDEPENDS:am62xx = "mc::k3r5-gp:ti-sci-fw:do_deploy"
do_deploy[mcdepends] = "${MCDEPENDS}"

SRC_URI = " \
    http://sources.toradex.com/tezi/${BPN}_${PV}.tar.xz \
    file://wrapup.sh \
    file://tezi.png \
    file://recovery-linux.sh \
    file://recovery-windows.bat \
    file://recovery/imx_usb.conf \
    file://recovery/mx6_usb_rom.conf \
    file://recovery/mx6_usb_sdp_spl.conf \
    file://recovery/mx6_usb_sdp_uboot.conf \
    file://recovery/mx7_usb_rom.conf \
    file://recovery/mx7_usb_sdp_uboot.conf \
    file://recovery/mx6ull_usb_rom.conf \
    file://recovery/mx6ull_usb_sdp_uboot.conf \
    file://recovery/uuu \
    file://recovery/uuu.exe \
"
SRC_URI:append:mx8-generic-bsp = " \
    file://recovery/uuu.auto \
"

SRC_URI[md5sum] = "11cf6d9b4b18b7f35f06ed91bfc0b3a8"
SRC_URI[sha256sum] = "064bff2e4cb4a0c0f8bceeaf6dd2cef1e682869e0b22d49e40bc6a8326ec14c9"

TEZI_RUN_DEPLOYDIR = "${DEPLOYDIR}/${BPN}"

inherit deploy linux-kernel-base nopackages

DEPENDS = "virtual/kernel"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

deploy_common () {
    install -d ${TEZI_RUN_DEPLOYDIR}/recovery
    install -m 644 ${WORKDIR}/wrapup.sh ${TEZI_RUN_DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.png ${TEZI_RUN_DEPLOYDIR}
    install -m 755 ${WORKDIR}/recovery-linux.sh ${TEZI_RUN_DEPLOYDIR}
    install -m 644 ${WORKDIR}/recovery-windows.bat ${TEZI_RUN_DEPLOYDIR}
}

do_deploy () {
    deploy_common

    install -m 644 ${WORKDIR}/recovery/imx_usb.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6_usb_rom.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6_usb_sdp_spl.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6_usb_sdp_uboot.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx7_usb_rom.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx7_usb_sdp_uboot.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6ull_usb_rom.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6ull_usb_sdp_uboot.conf ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${S}/README.imx_usb ${TEZI_RUN_DEPLOYDIR}/recovery/README
    install -m 755 ${S}/imx_usb ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${S}/imx_usb.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
}

do_deploy:am62xx () {
    deploy_common

    install -m 755 ${RECIPE_SYSROOT_NATIVE}/usr/bin/dfu-util ${TEZI_RUN_DEPLOYDIR}/recovery/
}

do_deploy:mx8-generic-bsp () {
    deploy_common

    install -m 755 ${WORKDIR}/recovery/uuu ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/recovery/uuu.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/uuu.auto ${TEZI_RUN_DEPLOYDIR}/recovery/
}

addtask deploy before do_build after do_install

COMPATIBLE_MACHINE = "(apalis|colibri|verdin)"

PACKAGE_ARCH = "${MACHINE_ARCH}"
