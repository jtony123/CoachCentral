package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import play.Configuration;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.S3Handler;

public class BackupRecovery extends Controller {

	
	@Inject 
	private  Configuration configuration;
	String filepath;
	private final HttpExecutionContext ec;
	
    @Inject
    public BackupRecovery(final HttpExecutionContext ec, Configuration configuration)
    {
        this.ec = ec;
        this.configuration = configuration;
        filepath = configuration.getString("filepath");
    }
    
    
    public CompletionStage<Result> backupPlayerGPSData() {
    	Logger.info(this.getClass().getName() + " - backupPlayerGPSData called ");
       	S3Handler s3 = new S3Handler();
    	s3.backupPlayerGPSData(filepath);
    	
    	return CompletableFuture.completedFuture(redirect(routes.Application.dashboard(0, "All")));
    }
    
    public CompletionStage<Result> recoverPlayerGPSData() {
    	Logger.info(this.getClass().getName() + " - backupPlayerGPSData called ");
       	S3Handler s3 = new S3Handler();
    	s3.recoverPlayerGPSData(filepath);
    	
    	return CompletableFuture.completedFuture(redirect(routes.Application.dashboard(0, "All")));
    }
    
    
    public CompletionStage<Result> backupPlayerPhotos() {
    	
    	Logger.info(this.getClass().getName() + " - backupPlayerPhotos called ");
    	S3Handler s3 = new S3Handler();
    	s3.backupPlayerPhotos();
    	
    	return CompletableFuture.completedFuture(redirect(routes.Application.dashboard(0, "All")));
    }
    

    public CompletionStage<Result> recoverPlayerPhotos() {
    	
    	Logger.info(this.getClass().getName() + " - recoverPlayerPhotos called ");
    	
    	S3Handler s3 = new S3Handler();
    	s3.recoverPlayerPhotos(filepath);
    	
    	
    	
//        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJJD5JUPE2GJANEJA", "2eUngdKAZetVoxhNdAXPOl83O9VGQuzdL95x6ukd");
//       
//        String s3Bucket = "coachcentralpersistentfilerepo";
//        AmazonS3 s3client = new AmazonS3Client(awsCredentials);
//        
//        List<Bucket> buckets = s3client.listBuckets();
//               
//        for (Bucket bucket : buckets) {
//        	System.out.println(" - " + bucket.getName());
//        }
//        
//        Bucket myBucket = s3client.listBuckets().get(0);
        
        // /Users/anthonyjackson/Desktop
//        String fileName = "test.txt";
//        s3client.putObject(new PutObjectRequest(myBucket.getName(), fileName, 
//        		new File("/Users/anthonyjackson/Desktop/test.txt")));
        
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(0);
//        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
//        PutObjectRequest putObjectRequest = new PutObjectRequest(myBucket.getName(),
//                "GPSData" + "/", emptyContent, metadata);
//        s3client.putObject(putObjectRequest);
//        
//        S3Object object = s3client.getObject(new GetObjectRequest(myBucket.getName(), "test.txt"));
//        InputStream objectData = object.getObjectContent();
//        
//        File file = new File("data/testfile.txt");
//        
//        try {
//			java.nio.file.Files.copy(objectData, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        IOUtils.closeQuietly(objectData, null);
        
        
		
	

//        ObjectListing listing = s3client.listObjects(buckets.get(0).getName(), "/");
//        Logger.info("Inside Bucket 0: " + listing);
//        ObjectNode result = Json.newObject();
//	    if(buckets != null){
//	    	for(int i = 0; i < buckets.size(); ++i){
//				result.put("bucket_"+i, buckets.get(i).getName());
//			}
//	    }
    	
    	return CompletableFuture.completedFuture(redirect(routes.Application.dashboard(0, "All")));
    }
    
    
    
    
    
    
}
