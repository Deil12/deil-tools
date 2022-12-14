#！ /bin/bash

export BASE_DIR=`cd $(dirname $0)/..; pwd`
export PSW="password"

usage() {
    echo "* * * * * * * 使 用 说 明: * * * * * * *"
    echo "sh serviceCryption.sh [enc|dec]"
    echo "* * * * * * * * * * * * * * * * * * * *"
    exit 1
}

encrypt(){
#  while getopts ":p:" opt
#  do
#      case $opt in
#          p)
#              PSW=$OPTARG;;
#          ?)
#          echo "UNKNOWN PARAMETER"
#          exit 1;;
#      esac
#  done

  echo 盐值:
  read key
  echo 明文:
  read enc
  echo 开始加密......
  java -cp ${BASE_DIR}/lib/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password==$key algorithm=PBEWithMD5AndDES input=$enc
}

decrypt(){
  echo 盐值:
  read key
  echo 密文:
  read dec
  echo 开始解密......
  java -cp ${BASE_DIR}/lib/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI input=$dec password=$key algorithm=PBEWithMD5AndDES
}

case "$1" in
  "enc")encrypt;;
  "dec")decrypt;;
  *)usage;;
esac