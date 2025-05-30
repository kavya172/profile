package com.galaxe.gxworkflow.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.galaxe.gxworkflow.dto.DatabaseConnectionDetailsDTO;
import com.galaxe.gxworkflow.entity.DatabaseDetails;

@Component
public class DatabaseConnectionUtil {

	public static DatabaseConnectionDetailsDTO getDataSource(DatabaseDetails databaseInfo) {

		DatabaseConnectionDetailsDTO dataSource = new DatabaseConnectionDetailsDTO();
		String url = null;
		String port = null;
		if (databaseInfo.getDatabaseType() != null) {
			switch (databaseInfo.getDatabaseType()) {
			case Constants.SQLSERVER:
				if (!StringUtils.isEmpty(databaseInfo.getServer())
						&& !StringUtils.isEmpty(databaseInfo.getDatabaseName())) {
					url = "jdbc:sqlserver://" + databaseInfo.getServer() + ";databaseName=" + databaseInfo.getDatabaseName();

				}
				break;
			case Constants.POSTGRESSQL:
				port = (databaseInfo.getPort() != null) ? databaseInfo.getPort() : "5432";
				if (!StringUtils.isEmpty(databaseInfo.getServer())
						&& !StringUtils.isEmpty(databaseInfo.getDatabaseName())
						&& !StringUtils.isEmpty(databaseInfo.getSchema())) {
					url = "jdbc:postgresql://" + databaseInfo.getServer() + ":" + port + "/"
							+ databaseInfo.getDatabaseName() + "?currentSchema=" + databaseInfo.getSchema();

				}
				break;
			case Constants.MONGODB:
				port = (databaseInfo.getPort() != null) ? databaseInfo.getPort() : "27017";
				if (!StringUtils.isEmpty(databaseInfo.getServer())
						&& !StringUtils.isEmpty(databaseInfo.getDatabaseName())) {
					url = "mongodb://" + databaseInfo.getServer() + ":" + port + "/"
							+ databaseInfo.getDatabaseName();

				}

				break;
			default:
				break;
			}
		}

		dataSource.setUrl(url);
		dataSource.setUsername(databaseInfo.getUsername());
		dataSource.setPassword(databaseInfo.getPassword());

		return dataSource;

	}
}
