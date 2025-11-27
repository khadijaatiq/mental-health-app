function getJWT() {
    return document.cookie
        .split(";")
        .map(c => c.trim().split("="))
        .find(c => c[0] === "JWT")?.[1] || null;
}

function logout() {
    document.cookie = "JWT=; expires=Thu, 01 Jan 1970; path=/;";
    window.location.href = "/login.html";
}

async function loadAllPosts() {
    const jwt = getJWT();
    const response = await fetch("/api/admin/posts", {
        headers: { "Authorization": "Bearer " + jwt }
    });

    const posts = await response.json();
    renderPosts(posts, "postList");
}

async function loadFlaggedPosts() {
    const jwt = getJWT();
    const response = await fetch("/api/admin/posts/flagged", {
        headers: { "Authorization": "Bearer " + jwt }
    });

    const posts = await response.json();
    renderPosts(posts, "flaggedList", true);
}

function renderPosts(posts, containerId, flagged = false) {
    const container = document.getElementById(containerId);

    container.innerHTML = posts.map(p => `
        <div class="post-card">
            <strong>${p.user?.username || "Unknown"}</strong>
            <div>${p.content}</div>

            ${flagged ? `<div><b>Flag:</b> ${p.flagReason}</div>` : ""}

            <button onclick="deletePost(${p.id})">Delete</button>
            ${flagged ? `<button onclick="resolvePost(${p.id})">Unflag</button>` : ""}
        </div>
    `).join("");
}

async function deletePost(id) {
    const jwt = getJWT();
    await fetch(`/api/admin/posts/${id}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + jwt }
    });

    loadAllPosts();
    loadFlaggedPosts();
}

async function resolvePost(id) {
    const jwt = getJWT();
    await fetch(`/api/admin/posts/${id}/resolve`, {
        method: "POST",
        headers: { "Authorization": "Bearer " + jwt }
    });

    loadFlaggedPosts();
}
