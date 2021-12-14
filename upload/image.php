<?php 
 
 //Constants for database connection
 define('DB_HOST','101.50.1.56');
 define('DB_USER','bryanyeh_user');
 define('DB_PASS','H@F6wmPKk6Rw7@V');
 define('DB_NAME','bryanyeh_database');
 define('UPLOAD_PATH', 'uploadimage/');
 
 //connecting to database 
 $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to connect');
 $response = array();
 if(isset($_GET['apicall'])){
 switch($_GET['apicall']){
 
 //if it is an upload call we will upload the image
 case 'uploadpic':
 
 if(isset($_FILES['pic']['name']) && isset($_POST['date'])){
 try{
 move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);
 $stmt = $conn->prepare("INSERT INTO images (image, date) VALUES (?,?)");
 $stmt->bind_param("ss", $_FILES['pic']['name'],$_POST['date']);
 if($stmt->execute()){
 $response['error'] = false;
 $response['message'] = 'Foto Sukses Terupload';
 }else{
 throw new Exception("Tidak Bisa Mengupload Foto");
 }
 }catch(Exception $e){
 $response['error'] = true;
 $response['message'] = 'Tidak Bisa Mengupload Foto';
 }
 
 }else{
 $response['error'] = true;
 $response['message'] = "Ada Parameter Yang Kurang";
 }
 break;
 
 //in this call we will fetch all the images 
 case 'getpics':
 
 $server_ip = gethostbyname(gethostname());
 $stmt = $conn->prepare("SELECT id, image, date FROM images");
 $stmt->execute();
 $stmt->bind_result($id, $image, $date);
 $images = array();
 
 while($stmt->fetch()){
 $temp = array();
 $temp['id'] = $id; 
 $temp['image'] = 'http://' . $server_ip . '/upload/'. UPLOAD_PATH . $image; 
 $temp['date'] = $date; 
 
 array_push($images, $temp);
 }
 
 //pushing the array in response 
 $response['error'] = false;
 $response['images'] = $images; 
 break; 
 
 default: 
 $response['error'] = true;
 $response['message'] = 'Invalid api call';
 }
 
 }else{
 header("HTTP/1.0 404 Not Found");
 echo "<h1>Connected</h1>";
 echo "The Database Connection is Successful.";
 exit();
 }
 
 //displaying the response in json 
 header('Content-Type: application/json');
 echo json_encode($response);
 