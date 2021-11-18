function helloWord() {
    window.alert("Hello Word")

}

function signIn() {
    event.preventDefault();
    var userName = document.getElementById("username-field");
    var passWord = document.getElementById("password-field");
    console.log("in signInfunction ");
    console.log(userName.value, passWord.value);
}

function signUp() {
    event.preventDefault();
    var userName = document.getElementById("username-field");
    var passWord = document.getElementById("password-field");
    console.log("in signInfunction ");
    console.log(userName.value, passWord.value);
}