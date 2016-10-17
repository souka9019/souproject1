/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\haku\\AndroidStudioProjects\\HelloWorld\\app\\src\\main\\aidl\\net\\npaka\\helloworld\\IMyService1.aidl
 */
package net.npaka.helloworld;
public interface IMyService1 extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements net.npaka.helloworld.IMyService1
{
private static final java.lang.String DESCRIPTOR = "net.npaka.helloworld.IMyService1";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an net.npaka.helloworld.IMyService1 interface,
 * generating a proxy if needed.
 */
public static net.npaka.helloworld.IMyService1 asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof net.npaka.helloworld.IMyService1))) {
return ((net.npaka.helloworld.IMyService1)iin);
}
return new net.npaka.helloworld.IMyService1.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setMessage(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements net.npaka.helloworld.IMyService1
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void setMessage(java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_setMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void setMessage(java.lang.String msg) throws android.os.RemoteException;
}
