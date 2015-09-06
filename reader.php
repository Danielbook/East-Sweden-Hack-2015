<?php 

function setPost()
{
    echo '<div class="container">'; 
    echo '<div class="row">';
    echo  '<div class="col-md-6">';

    $piccounter = 1;
    $name = "image";
    $name = "image".$piccounter.".jpg";

    $image = $name;
    // Read image path, convert to base64 encoding
    $imageData = base64_encode(file_get_contents($image));

    // Format the image SRC:  data:{mime};base64,{data};
    $src = 'data: '.mime_content_type($image).';base64,'.$imageData;

    // Echo out a sample image
    echo '<img class="img-responsive" class="imageCat" src="' . $src . '">';

    $piccounter = $piccounter + 1;

    echo '</div>';

        echo '<div class="col-md-6" style="border-style:none;" >';

        echo '<h2 class="date" >2015-09-05 21:37  </h2> ';

        echo '<p class="message" >Mjau! Jag vill in det är kallt ute  </p> ';

        echo '<img class="center-block" class="img-position" src="css/bubble.png" alt="">';

        $add = 1;
    $counter = 0;
    // file example 1: read a text file into an array, with 
    // each line in a new element

    // första raden är tom, andra regn och trejde temp
    $filename="data.txt"; 
    $lines = array(); 
    $file = fopen($filename, "r"); 
    while(!feof($file)) { 

    //read file line by line into a new array element 
        $lines[] = fgets($file, 4096); 
        $counter = $counter + $add;
    } 

    if ($lines[($counter - $add)] < 0 && $lines[($counter - add)] >= 0 )
    {
        echo '<h> snö </h>';
    }
    elseif ($lines[($counter - 2*$add)] > 0)
    {
        echo '<img class="img-position-top" src="moln.png" alt="">';
    }
    elseif ($lines[($counter - $add)] <= 5) 
    {
        echo '<h>Kallt </h>';
    }
    elseif ($lines[($counter - $add)] >= 15 ) 
    {
        echo '<h>varmt </h>';
    } 

    elseif ($lines[($counter - $add)] < 15 && $lines[($counter - $add)] > 5)
    {
        echo '<h>moln </h>';
    }

    echo '</div>';

}


?>