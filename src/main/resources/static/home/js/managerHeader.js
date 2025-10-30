document.querySelectorAll(".btn-menu").forEach((btn) => {
	btn.addEventListener("click", (e) => {
		btn.classList.toggle("active");
	});
});

const toggleMenu = document.getElementById("toggleNav");
const menu = document.getElementById("nav");

toggleMenu.addEventListener("click", () => {
	menu.classList.toggle("show");
});