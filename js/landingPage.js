function helloWord() {
    window.alert("Hello Word")

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
    $.post("---",
    {
      //user name data to pass along to server
      user: userName.toLowerCase(),
    },
    //if the post succeds redirect to either drawing page or watching page
    function(data){
      window.location.href = data;
    });
}


function signUp() {
    event.preventDefault();
    var userName = document.getElementById("username-field");
    var passWord = document.getElementById("password-field");
    console.log("in signInfunction ");
    console.log(userName.value, passWord.value);
}
