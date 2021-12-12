<?php 
 
 //Constants for database connection
 define('DB_HOST','localhost');
 define('DB_USER','root');
 define('DB_PASS','');
 define('DB_NAME','uploadkamera');
 
 define('UPLOAD_PATH', 'uploads/');
 
 //connecting to database 
 $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to connect');
 
 //An array to display the response
 $response = array();
 
 //if the call is an api call 
 if(isset($_GET['apicall'])){
 
 //switching the api call 
 switch($_GET['apicall']){
 
 //if it is an upload call we will upload the image
 case 'uploadpic':
 
 //first confirming that we have the image and tags in the request parameter
 if(isset($_FILES['pic']['name']) && isset($_POST['tags'])){
 
 //uploading file and storing it to database as well 
 try{
 move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);
 $stmt = $conn->prepare("INSERT INTO images (image, tags) VALUES (?,?)");
 $stmt->bind_param("ss", $_FILES['pic']['name'],$_POST['tags']);
 if($stmt->execute()){
 $response['error'] = false;
 $response['message'] = 'File Telah Terupload Dengan Sukses';
 }else{
 throw new Exception("Tidak Bisa Mengupload File");
 }
 }catch(Exception $e){
 $response['error'] = true;
 $response['message'] = 'Tidak Bisa Mengupload File';
 }
 
 }else{
 $response['error'] = true;
 $response['message'] = "Ada Parameter Yang Masih Kosong";
 }
 
 break;
 
 //in this call we will fetch all the images 
 case 'getpics':
 
 //getting server ip for building image url 
 $server_ip = gethostbyname(gethostname());
 
 //query to get images from database
 $stmt = $conn->prepare("SELECT id, image, tags FROM images");
 $stmt->execute();
 $stmt->bind_result($id, $image, $tags);
 
 $images = array();
 
 //fetching all the images from database
 //and pushing it to array 
 while($stmt->fetch()){
 $temp = array();
 $temp['id'] = $id; 
 $temp['image'] = 'http://' . $server_ip . '/UploadImage/'. UPLOAD_PATH . $image; 
 $temp['tags'] = $tags; 
 
 array_push($images, $temp);
 }
 
 //pushing the array in response 
 $response['error'] = false;
 $response['images'] = $images; 
 break; 
 
 default: 
 $response['error'] = true;
 $response['message'] = 'API Call Invalid';
 }
 
 }else{
 header("HTTP/1.0 404 Not Found");
 echo "<h1>404 Not Found</h1>";
 echo "The page that you have requested could not be found.";
 exit();
 }
 
 //displaying the response in json 
 header('Content-Type: application/json');
 echo json_encode($response);