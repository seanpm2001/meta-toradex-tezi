DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://gitlab.toradex.int/rd/tezi/qt-tezi.git;branch=qt5-port;protocol=http \
    file://defaults \
    file://rc.local \
    file://udhcpd.conf \
    file://ifplugd.dhcp.action \
    file://ifplugd.usb.action \
"

SRCREV = "${AUTOREV}"
SRCREV_use-head-next = "${AUTOREV}"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=81f0d32e0eab9775391c3bdeb681aadb"

S = "${WORKDIR}/git"
inherit qmake5

TOUCH = ' ${@bb.utils.contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests", "",d)}'

DEPENDS += " \
    libusbgx \
    rapidjson \
    qtbase \
"

RDEPENDS_${PN} += " \
    flash-wince \
    util-linux-sfdisk \
    util-linux-blkid \
    util-linux-blkdiscard \
    ${TOUCH} \
    ttf-vera \
"

FILES_${PN} = " \
    ${sysconfdir} \
    ${sysconfdir}/rc.local \
    ${sysconfdir}/default/tezi \
    ${sysconfdir}/udhcpd.conf \
    ${sysconfdir}/ifplugd \
    ${bindir} \
    ${datadir}/tezi/ \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/../build/tezi ${D}${bindir}/
    install -d ${D}${datadir}/tezi/keymaps/
    install -m 0644 ${S}/keymaps/*qmap ${D}${datadir}/tezi/keymaps/

    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/rc.local ${D}${sysconfdir}

    install -d ${D}${sysconfdir}/default/
    install -m 0755 ${WORKDIR}/defaults ${D}${sysconfdir}/default/tezi

    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/ifplugd
    install -m 0755 ${WORKDIR}/ifplugd.dhcp.action ${D}${sysconfdir}/ifplugd
    install -m 0755 ${WORKDIR}/ifplugd.usb.action ${D}${sysconfdir}/ifplugd

}

FILES_${PN}-dbg += "${bindir}/.debug"
