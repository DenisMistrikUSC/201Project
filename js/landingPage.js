/*
Takes user to start up page when button is clicked
*/
function login() {
  window.location.href = "signIn.html";
}

/*
This function retrives user input and validates with the server
*/
function signIn() {
    event.preventDefault();
    var userName = document.getElementById("username-field").value;
    var passWord = document.getElementById("password-field").value;
    /*
    Post should request the JSON file containing the User and Icon
    to verify to user has an account
    */
    signInHTTPRequest(userName);
}


function signUp() {
    event.preventDefault();
    var userName = document.getElementById("username-field").value;
    var passWord = document.getElementById("password-field").value;

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
    window.location.href = data;
  }).fail(function (jqXHR){
    // add no user handling here
    let invalidUser = document.getElementById("username-field");
    invalidUser.classList.add('is-invalid');
    let invalidPassword = document.getElementId("password-field");
    invalidPassword.classList.add('is-invalid');
  });
}

/*
Gets a random username back from the server then redirects to one of the game
pages
*/
function playAsGuest() {
  signInHTTPRequest("");
}
