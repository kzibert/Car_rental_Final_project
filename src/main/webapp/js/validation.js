function validation() {
	  var regex = /^[A-Za-zА-Я-а-яёЁ]{1,30}$/;
	  let form = document.forms["regform"];
      let name = form["name"].value;
      let surname = form["surname"].value;
      let password =form["password"].value;
      let passwordCheck = form["password_check"].value;

      if (!name.match(regex)) {
        alert("Wrong name format! Name should contain up to 30 latin or cyrillic letters");
        return false;
      }
      if (!surname.match(regex)) {
        alert("Wrong surname format! Surname should contain up to 30 latin or cyrillic letters");
        return false;
      }
      if (!(password === passwordCheck)) {
        alert("Please recheck password! Password should contain 8-30 latin letters, digits or symbols.");
        return false;
      } else {
        return true;
      }
    }