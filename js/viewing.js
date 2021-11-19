function setup(){
  //size determined by html tag/bootstrap
  let canvas = createCanvas(300,300);
  while(!roundOver){
    getImageFromServer(canvas);
  }
}
/*
HTML get request for images of the active drawer's screenshots
*/
function getImageFromServer(canvas){
  //needs filename of image json
  $.get("---",function(data){
    view(canvas, data);
  });
)
}
/*
This function receives a photo from the server and overrides the current photo
in the html
*/
function view(canvas, srcImg){
  let image = new Image();
  //assign dataURL from server
  image.src = srcImg;
  //places dataURl in html page
  canvas.innerHTML = "<img src='" + image.src + "'>";
}
/*
This should get an alert from the server that this the round is over and
thus the loop in the setup function can terminate
*/
function roundOver() {
  let roundOver = false;


}
