package com.ubhave.datastore.file;

import java.io.IOException;
import java.util.List;

import android.content.Context;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.datahandler.config.DataStorageConstants;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datastore.DataStorageInterface;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class FileDataStorage implements DataStorageInterface
{	
//	private final Context context;
//	private final DataHandlerConfig config;
	
	private final FileVault vault;
	private final FileStoreWriter fileStoreWriter;
	private final FileStoreCleaner fileStoreCleaner;
	private final FileStoreSearcher fileSearch;

	public FileDataStorage(final Context context, final Object fileTransferLock)
	{
//		this.config = DataHandlerConfig.getInstance();
		
		this.vault = new FileVault();
		this.fileStoreCleaner = new FileStoreCleaner(fileTransferLock, vault);
		this.fileStoreWriter = new FileStoreWriter(vault, fileStoreCleaner);
		this.fileSearch = new FileStoreSearcher(context, vault);
	}
	
	@Override
	public void logSensorData(final SensorData data, final DataFormatter formatter) throws DataHandlerException
	{
		String sensorName;
		try
		{
			sensorName = SensorUtils.getSensorName(data.getSensorType());
		}
		catch (ESException e)
		{
			sensorName = DataStorageConstants.UNKNOWN_SENSOR;
		}
		String directoryName = sensorName;
		fileStoreWriter.writeData(directoryName, formatter.toString(data));
	}

	@Override
	public void logExtra(final String tag, final String data) throws DataHandlerException
	{
		fileStoreWriter.writeData(tag, data);
	}
	
	@Override
	public void onDataUploaded()
	{}

	@Override
	public String prepareDataForUpload()
	{
		return fileStoreCleaner.moveDataForUpload();
	}

	@Override
	public List<SensorData> getRecentSensorData(int sensorId, long startTimestamp) throws DataHandlerException, ESException, IOException
	{
		return fileSearch.getRecentSensorData(sensorId, startTimestamp);
	}
}
