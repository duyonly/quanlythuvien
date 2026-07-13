document.getElementById("register-form")
.addEventListener("submit", async (e) => {

    e.preventDefault();

    const data = {
        fullname: document.getElementById("fullname").value,
        email: document.getElementById("email").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    console.log(data);

    const response = await fetch(
        "http://localhost:8080/api/users/register",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }
    );

    if(response.ok){
        alert("Đăng ký thành công!");
        window.location.href = "index.html";
    }
});