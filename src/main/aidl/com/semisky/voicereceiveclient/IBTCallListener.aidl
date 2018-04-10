package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface IBTCallListener {

   //断开手机连接
   int DisconnectThePhone();

   //关闭蓝牙连接
   int cutBTCallConnect();

   //打电话(号码)
   int callByNumber(String number);

   //打电话(联系人)
   int callByContacts(String contact);
}