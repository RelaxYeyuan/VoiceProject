package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface IBTCallListener {

   //查看通话记录
   int queryCallRecords();

   //打电话(号码)
   int callByNumber(String number);

   //打电话(联系人)
   int callByContacts(String contact);
}