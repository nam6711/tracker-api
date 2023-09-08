package com.example.restservice.storage;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.IOException; 

public interface StorageService {
	void store(File file) throws JSchException, SftpException, IOException;

}