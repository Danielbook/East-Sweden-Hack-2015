
<?php

header('Content-type: text/html; charset=utf-8');

$myfile = fopen("hej.txt", "r") or die("Unable to open file!");
// Output one line until end-of-file
while(!feof($myfile)) {
   echo fgets($myfile) . "<br>";
}
fclose($myfile);

?>
