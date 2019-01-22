// IMyMemoryService.aidl
package com.example.liunan.sharedmemoyserver;

// Declare any non-default types here with import statements

interface IMyMemoryService {
    void writeByte(int offset,byte num);
    SharedMemory getMemory();
}
