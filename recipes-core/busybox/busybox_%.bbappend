FILESEXTRAPATHS_prepend := "${THISDIR}/files:" 
SRC_URI_append = "\
    file://resize.cfg \
    file://mdev.cfg \
    file://tinyinit.cfg \
    file://ifplugd.cfg \
    file://utils.cfg \
"

