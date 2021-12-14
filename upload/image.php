<?php 
 
 define('DB_HOST','localhost');
 define('DB_USER','root');
 define('DB_PASS','');
 define('DB_NAME','upload');
 define('UPLOAD_PATH', 'uploadimage/');
 
 $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to connect');
 
 $response = array(); 
 if(isset($_GET['apicall'])){ 
 switch($_GET['apicall']){
 
 case 'uploadpic':
 if(isset($_FILES['pic']['name']) && isset($_POST['upload_date'])){
 
 try{
 move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);
 $stmt = $conn->prepare("INSERT INTO images (image, upload_date) VALUES (?,?)");
 $stmt->bind_param("ss", $_FILES['pic']['name'],$_POST['upload_date']);
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
 

 case 'getpics': 
 $server_ip = gethostbyname(gethostname());
 $stmt = $conn->prepare("SELECT id, image, upload_date FROM images");
 $stmt->execute();
 $stmt->bind_result($id, $image, $upload_date);
 $images = array();

 while($stmt->fetch()){
 $temp = array();
 $temp['id'] = $id; 
 $temp['image'] = 'http://' . $server_ip . '/upload/'. UPLOAD_PATH . $image; 
 $temp['upload_date'] = $upload_date; 
 array_push($images, $temp);
 }
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
 
 header('Content-Type: application/json');
 echo json_encode($response);