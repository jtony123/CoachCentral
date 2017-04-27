package utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.avaje.ebeaninternal.server.type.ScalarTypeJsonMap.Blob;

import play.Logger;

import java.util.List;

import models.Player;

public class S3Handler {
	
	
	private String accessKey = "AKIAJJD5JUPE2GJANEJA";
	private String secretKey = "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";
	AWSCredentials awsCredentials;
	
	private String ccBucket = "coachcentralpersistentfilerepo";
	
	
	public S3Handler(){
		
		awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		
	}
	
	public void saveFile(String fileName, File file){
	
		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		Bucket myBucket = s3client.listBuckets().get(0);
	
		s3client.putObject(new PutObjectRequest(myBucket.getName(), 
							fileName, 
							file));
		
	}
	
	public File getFile(String fileName){
		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		Bucket myBucket = s3client.listBuckets().get(0);
		
		S3Object object = s3client.getObject(new GetObjectRequest(myBucket.getName(), fileName));
	      
		InputStream objectData = object.getObjectContent();
	   
		File file = new File(fileName);
	      
		try {
			java.nio.file.Files.copy(objectData, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      IOUtils.closeQuietly(objectData, null);
		
		return null;
		
	}
	
	
	public void recoverPlayerGPSData(String tmpfilePath) {
		
		Logger.info(this.getClass().getName() + " - recoverPlayerGPSData called ");

		String playerGPSDirName = "GPSData/";
		
		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		Bucket myBucket = s3client.listBuckets().get(0);

		List<Player> players = Player.find.all();
		for (Player player : players) {

			String fileName = player.filename;
			File afile = new File(tmpfilePath + fileName);
			
			try {

				// get the file from s3 and store it in tmp
				S3Object object = s3client.getObject(new GetObjectRequest(myBucket.getName(), playerGPSDirName+fileName));
				InputStream objectData = object.getObjectContent();
				java.nio.file.Files.copy(objectData, afile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				IOUtils.closeQuietly(objectData, null);

			} catch (Exception e) {
				// e.printStackTrace();
			} finally {

			}
		}
		
	}
	
	public void backupPlayerGPSData(String tmpfilePath) {
		
		Logger.info(this.getClass().getName() + " - backupPlayerGPSData called ");

		String playerGPSDirName = "GPSData/";

		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		Bucket myBucket = s3client.listBuckets().get(0);

		List<Player> players = Player.find.all();
		for (Player player : players) {
			
	    String fileName = player.filename;
	    File file = new File(tmpfilePath + fileName);
	    InputStream content = null;
	    
	    try {
			ObjectMetadata metadata = new ObjectMetadata();
			
			//content = file.getInputStream();
			byte[] b = java.nio.file.Files.readAllBytes(file.toPath());
	    	content = new ByteArrayInputStream(b);
	    	metadata.setContentLength(b.length);
	        s3client.putObject(new PutObjectRequest(myBucket.getName(), playerGPSDirName+fileName, 
	        		content, metadata));
	        Logger.info("S3Handler backed up player :" + player.playername);
	    } catch (Exception e) {
	    	//e.printStackTrace();
			Logger.warn("Exception thrown in S3Handler line 103 for player " + player.playername);
		} finally {
			
		}
			
			
			// copy the file to the tmp folder
//			String fileName = player.filename;
//			InputStream content = null;
//			try {
//
//				byte[] blob = player.playerPhoto;
//
//				ObjectMetadata metadata = new ObjectMetadata();
//				metadata.setContentLength(blob.length);
//				
//				content = new ByteArrayInputStream(blob);
//				PutObjectRequest putObjectRequest = new PutObjectRequest(myBucket.getName(),
//						playerImagesDirName + fileName, content, metadata);
//				s3client.putObject(putObjectRequest);
//			} catch (Exception e) {
//				Logger.info("Exception thrown in S3Handler line 103 for player " + player.playername);
//			} finally {
//				if (content != null) {
//					try {
//						content.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				
//			}
			
			
		}

	}
	
	
	
	public void backupPlayerPhotos() {
		
		Logger.info(this.getClass().getName() + " - backupPlayerPhotos called ");

		String playerImagesDirName = "PlayerImages/";

		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		Bucket myBucket = s3client.listBuckets().get(0);

		List<Player> players = Player.find.all();
		for (Player player : players) {

			// copy the file to the tmp folder
			String fileName = player.playerPhotofilename;
			InputStream content = null;
			try {

				byte[] blob = player.playerPhoto;

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(blob.length);
				
				content = new ByteArrayInputStream(blob);
				PutObjectRequest putObjectRequest = new PutObjectRequest(myBucket.getName(),
						playerImagesDirName + fileName, content, metadata);
				s3client.putObject(putObjectRequest);
			} catch (Exception e) {
				Logger.info("Exception thrown in S3Handler line 103 for player " + player.playername);
			} finally {
				if (content != null) {
					try {
						content.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}

	}
	
	
	
	public void recoverPlayerPhotos(String tmpfilePath) {
		
		String playerImagesDirName = "PlayerImages/";
		

		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		Bucket myBucket = s3client.listBuckets().get(0);

		List<Player> players = Player.find.all();
		for (Player player : players) {

			String fileName = player.playerPhotofilename;
			File afile = new File(tmpfilePath + fileName);
			
			try {

				// get the file from s3 and store it in tmp
				S3Object object = s3client.getObject(new GetObjectRequest(myBucket.getName(), playerImagesDirName+fileName));
				InputStream objectData = object.getObjectContent();

				
				try {
					java.nio.file.Files.copy(objectData, afile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					Logger.info("Exception thrown in S3Handler line 95 ");
					// e.printStackTrace();
				}
				IOUtils.closeQuietly(objectData, null);

				// now copy it from tmp to the db
				if (afile != null) {

					if (afile.length() > 0) {
						
						player.playerPhotofilename = fileName;
						player.playerPhoto = new byte[(int) afile.length()];
						Logger.info(this.getClass().getName() + " - Updated photo for " + player.playername);
						InputStream inStream = null;
						try {
							inStream = new BufferedInputStream(new FileInputStream(afile));
							inStream.read(player.playerPhoto);
						} catch (IOException e) {
							Logger.info("Exception thrown in S3Handler line 116 ");
							// e.printStackTrace();
						} finally {
							if (inStream != null) {
								try {
									inStream.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						player.save();
					}
				}

			} catch (Exception e) {
				// e.printStackTrace();
			} finally {
				afile.delete();
			}

		}
	}

}
