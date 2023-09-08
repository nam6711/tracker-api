package com.example.restservice.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream; 
import java.nio.file.Path;
import java.nio.file.Paths; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class FileSystemStorageService implements StorageService {

	private Path rootLocation;

	private String remoteHost = "162.144.3.38";
	private String username = "ritlabtr";
	private String password = "Hithere@2828"; 

	@Autowired
	public FileSystemStorageService() {
		StorageProperties properties = new StorageProperties();
		this.rootLocation = Paths.get(properties.getLocation());
	}
	 
    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        String knownHostsFileName = "src/main/resources/known_hosts";
        if (knownHostsFileName != null && new File(knownHostsFileName).exists()) {
            jsch.setKnownHosts(knownHostsFileName);
            System.out.println("KnownHostsFile added");
        }
        Session jschSession = jsch.getSession(username, remoteHost);
        jschSession.setPassword(password);
        // dangerous but idk what else to do
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        jschSession.setConfig(config);
        jschSession.connect(); 
        return (ChannelSftp) jschSession.openChannel("sftp");
    }
 
    @Override
    public void store(File file) throws JSchException, SftpException, IOException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();
      
        String remoteDir = "www/"; 
        System.out.println("SAVED " + file.getName());

		channelSftp.put(file.getAbsolutePath(), remoteDir + file.getName());      

        channelSftp.exit();
    }   
}
