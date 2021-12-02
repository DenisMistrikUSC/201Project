/*
Takes user to start up page when button is clicked
*/
function login() {
  window.location.href = "signIn.html";
}

function signUpRedirect() {
    window.location.href = "signUp.html";
}

/*
This function retrives user input and validates with the server
*/
function signIn() {
    event.preventDefault();
    let userName = document.getElementById("username-field").value;
    let passWord = document.getElementById("password-field").value;
    /*
    Post should request the JSON file containing the User and Icon
    to verify to user has an account
    */
    signInHTTPRequest(userName);

}
function guestName(){
  let userName = document.getElementId("username-field").value;
  let newUser = "g-"+userName;
}


function signUp() {
    event.preventDefault();
    let userName = document.getElementById("username-field").value;
    let passWord = document.getElementById("password-field").value;

}
/*
Note to Backend: if username is blank player is a guest
*/
function signInHTTPRequest(username) {
  $.post("---",
  {
    //user name data to pass along to server
    user: username.toLowerCase(),
  },
  /*
  if the post succeeds redirect to either drawing page or watching page,
  otherwise alert user that their username or password is incorrect when server
  sends back 401 response
  */
  function(data){
    //place game page link
    location.href = ".html";
  }).fail(function (jqXHR){
    // add no user handling here
    let invalidUser = document.getElementById("username-field");
    invalidUser.classList.add('is-invalid');
    let invalidPassword = document.getElementId("password-field");
    invalidPassword.classList.add('is-invalid');
  });
}
/*
Saves data to sessionStorage, which is unique per client
*/
function setUsername(username){
  sessionStorage.setIten('username',username)
}
/*
To be called on the game page, this get the users username from the session
*/
function getUsername(){
  let data = sessionStorage.getItem('username');
  //place id of username el in DOM
  document.getElementById("").innerHTML() = data;
}

/*
Attaches g- to user input and send to server
*/
function playAsGuest() {
  let username = document.getElementById("username-field").value;
  let newuser = "g-" + username.toLowerCase().trim();
  signInHTTPRequest(newuser);
}



//ignore below just another way


/*
This function should get username from the server then replace the dom element on
the game page that contains the username
*/
function getUserName(){
  $.get("---",function(data){
    /*
    looking for dom element that shows username
    */
    documenent.getElementbyId("username").innerHTML() = data;
  });
}
