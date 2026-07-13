document.getElementById("forgot-form")
.addEventListener("submit", async (e) => {

    e.preventDefault();

    const data = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        newPassword: document.getElementById("new-password").value
    };

    try {
        const response = await fetch(
            "http://localhost:8080/api/users/forgot-password",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            }
        );

        const result = await response.text();
        console.log(result);

        if (response.ok) {
            alert("Đổi mật khẩu thành công!");
            window.location.href = "index.html";
        } else {
            alert("Sai username hoặc email!");
        }

    } catch (error) {
        console.error(error);
        alert("Không kết nối được server!");
    }
});