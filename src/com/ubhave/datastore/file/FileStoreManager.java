package com.ubhave.datastore.file;

import android.content.Context;

import com.ubhave.datahandler.ESDataManager;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datastore.DataStorageInterface;
import com.ubhave.sensormanager.ESException;

public class FileStoreManager extends ESDataManager
{
	public FileStoreManager(final Context context) throws ESException, DataHandlerException
	{
		super(context);
	}
	
	@Override
	protected DataStorageInterface getStorage()
	{
		return new FileDataStorage(fileTransferLock);
	}
}
