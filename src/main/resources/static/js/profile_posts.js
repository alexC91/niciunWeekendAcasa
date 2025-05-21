document.addEventListener("DOMContentLoaded", () => {
    // Elements
    const postForm = document.getElementById("post-form")
    const postContent = document.getElementById("post-content")
    const postsContainer = document.getElementById("posts-container")
    const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content")
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content")

    // Import jQuery
    const $ = window.jQuery

    // Set up AJAX with CSRF token
    function setupAjaxCsrf() {
        const token = document.querySelector("meta[name='_csrf']").getAttribute("content")
        const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content")

        // Set up jQuery AJAX defaults
        $.ajaxSetup({
            beforeSend: (xhr) => {
                xhr.setRequestHeader(header, token)
            },
        })
    }

    setupAjaxCsrf()

    // Format date
    function formatDate(dateString) {
        const date = new Date(dateString)
        const now = new Date()
        const diffMs = now - date
        const diffSec = Math.floor(diffMs / 1000)
        const diffMin = Math.floor(diffSec / 60)
        const diffHour = Math.floor(diffMin / 60)
        const diffDay = Math.floor(diffHour / 24)

        if (diffSec < 60) {
            return "just now"
        } else if (diffMin < 60) {
            return `${diffMin} minute${diffMin > 1 ? "s" : ""} ago`
        } else if (diffHour < 24) {
            return `${diffHour} hour${diffHour > 1 ? "s" : ""} ago`
        } else if (diffDay < 7) {
            return `${diffDay} day${diffDay > 1 ? "s" : ""} ago`
        } else {
            return date.toLocaleDateString("en-US", {
                year: "numeric",
                month: "short",
                day: "numeric",
            })
        }
    }

    // Create post
    if (postForm) {
        postForm.addEventListener("submit", (e) => {
            e.preventDefault()

            if (!postContent.value.trim()) {
                alert("Please enter some content for your post.")
                return
            }

            // Use jQuery's $.ajax for better compatibility
            $.ajax({
                url: "/create-post",
                type: "POST",
                data: {
                    content: postContent.value,
                    [csrfHeader]: csrfToken, // Include CSRF token in the data
                },
                success: (response) => {
                    console.log("Post created successfully:", response)

                    // Clear the form
                    postContent.value = ""

                    // Create new post HTML
                    const postHtml = createPostHtml(
                        response.postId,
                        response.userName,
                        response.userImage || "https://bootdey.com/img/Content/avatar/avatar3.png",
                        response.content,
                        response.createdAt,
                        0,
                        0,
                        [],
                    )

                    // Add to the top of the posts container
                    if (postsContainer) {
                        postsContainer.insertAdjacentHTML("afterbegin", postHtml)

                        // Initialize the new post's comment form
                        const newPost = postsContainer.querySelector(`#post-${response.postId}`)
                        if (newPost) {
                            initializeCommentForm(newPost)
                        }

                        // Initialize other interactive elements for the new post
                        initializeLikeButtons()
                        initializeShareButtons()
                        initializeCommentToggles()
                    }
                },
                error: (xhr, status, error) => {
                    console.error("Error creating post:", xhr.responseText)
                    alert("Error creating post: " + (xhr.responseText || error))
                },
            })
        })
    }

    // Create post HTML
    function createPostHtml(postId, userName, userImage, content, createdAt, likesCount, sharesCount, comments) {
        const formattedDate = formatDate(createdAt)

        let commentsHtml = ""
        if (comments && comments.length > 0) {
            comments.forEach((comment) => {
                commentsHtml += `
                <div class="comment" id="comment-${comment.id}">
                    <div class="comment-user">
                        <img src="${comment.user.profilePhoto || "https://bootdey.com/img/Content/avatar/avatar3.png"}" alt="${comment.user.firstName} ${comment.user.lastName}" class="comment-user-img">
                        <span class="comment-username">${comment.user.firstName} ${comment.user.lastName}</span>
                    </div>
                    <div class="comment-content">${comment.content}</div>
                    <div class="comment-date">${formatDate(comment.createdAt)}</div>
                </div>
                `
            })
        }

        return `
        <li id="post-${postId}">
            <div class="timeline-time">
                <span class="date">${new Date(createdAt).toLocaleDateString()}</span>
                <span class="time">${new Date(createdAt).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}</span>
            </div>
            <div class="timeline-icon">
                <a href="javascript:;">&nbsp;</a>
            </div>
            <div class="timeline-body">
                <div class="timeline-header">
                    <span class="userimage">
                        <img src="${userImage}" alt="">
                    </span>
                    <span class="username">
                        <a href="javascript:;">${userName}</a>
                        <small></small>
                    </span>
                    <span class="pull-right text-muted">Just posted</span>
                </div>
                <div class="timeline-content">
                    <p>${content}</p>
                </div>
                <div class="timeline-likes">
                    <div class="stats-right">
                        <span class="stats-text">${sharesCount} Shares</span>
                        <span class="stats-text"><span class="comment-count">${comments ? comments.length : 0}</span> Comments</span>
                    </div>
                    <div class="stats">
                        <span class="fa-stack fa-fw stats-icon">
                            <i class="fa fa-circle fa-stack-2x text-danger"></i>
                            <i class="fa fa-heart fa-stack-1x fa-inverse t-plus-1"></i>
                        </span>
                        <span class="fa-stack fa-fw stats-icon">
                            <i class="fa fa-circle fa-stack-2x text-primary"></i>
                            <i class="fa fa-thumbs-up fa-stack-1x fa-inverse"></i>
                        </span>
                        <span class="stats-total likes-count">${likesCount}</span>
                    </div>
                </div>
                <div class="timeline-footer">
                    <a href="javascript:;" class="m-r-15 text-inverse-lighter like-button" data-post-id="${postId}">
                        <i class="fa fa-thumbs-up fa-fw fa-lg m-r-3"></i> Like
                    </a>
                    <a href="javascript:;" class="m-r-15 text-inverse-lighter comment-toggle">
                        <i class="fa fa-comments fa-fw fa-lg m-r-3"></i> Comment
                    </a>
                    <a href="javascript:;" class="m-r-15 text-inverse-lighter share-button" data-post-id="${postId}">
                        <i class="fa fa-share fa-fw fa-lg m-r-3"></i> Share
                    </a>
                </div>
                <div class="timeline-comment-box">
                    <div class="user">
                        <img src="${userImage}" alt="">
                    </div>
                    <div class="input">
                        <form class="comment-form" data-post-id="${postId}">
                            <div class="input-group">
                                <input type="text" class="form-control rounded-corner comment-input" placeholder="Write a comment...">
                                <span class="input-group-btn p-l-10">
                                    <button class="btn btn-primary f-s-12 rounded-corner" type="submit">Comment</button>
                                </span>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="comments-container">
                    ${commentsHtml}
                </div>
            </div>
        </li>
        `
    }

    // Initialize comment forms for existing posts
    function initializeCommentForms() {
        window
            .jQuery(document)
            .querySelectorAll(".comment-form")
            .forEach((form) => {
                initializeCommentForm(form.closest("li"))
            })
    }

    // Initialize comment form for a specific post
    function initializeCommentForm(postElement) {
        const form = postElement.querySelector(".comment-form")
        const postId = form.getAttribute("data-post-id")
        const commentInput = form.querySelector(".comment-input")
        const commentsContainer = postElement.querySelector(".comments-container")
        const commentCountElement = postElement.querySelector(".comment-count")

        form.addEventListener("submit", (e) => {
            e.preventDefault()

            if (!commentInput.value.trim()) {
                alert("Please enter a comment.")
                return
            }

            window.jQuery.ajax({
                url: "/create-comment",
                type: "POST",
                data: {
                    content: commentInput.value,
                    postId: postId,
                },
                success: (response) => {
                    // Clear the input
                    commentInput.value = ""

                    // Create comment HTML
                    const commentHtml = `
                    <div class="comment" id="comment-${response.commentId}">
                        <div class="comment-user">
                            <img src="${response.userImage || "https://bootdey.com/img/Content/avatar/avatar3.png"}" alt="${response.userName}" class="comment-user-img">
                            <span class="comment-username">${response.userName}</span>
                        </div>
                        <div class="comment-content">${response.content}</div>
                        <div class="comment-date">${formatDate(response.createdAt)}</div>
                    </div>
                    `

                    // Add to comments container
                    commentsContainer.insertAdjacentHTML("beforeend", commentHtml)

                    // Update comment count
                    const currentCount = Number.parseInt(commentCountElement.textContent) || 0
                    commentCountElement.textContent = currentCount + 1
                },
                error: (xhr) => {
                    alert("Error creating comment: " + xhr.responseText)
                },
            })
        })
    }

    // Initialize like buttons
    function initializeLikeButtons() {
        window
            .jQuery(document)
            .querySelectorAll(".like-button")
            .forEach((button) => {
                button.addEventListener("click", function () {
                    const postId = this.getAttribute("data-post-id")
                    const likesCountElement = this.closest("li").querySelector(".likes-count")

                    window.jQuery.ajax({
                        url: "/like-post",
                        type: "POST",
                        data: {
                            postId: postId,
                        },
                        success: (response) => {
                            likesCountElement.textContent = response.likesCount
                        },
                        error: (xhr) => {
                            alert("Error liking post: " + xhr.responseText)
                        },
                    })
                })
            })
    }

    // Initialize share buttons
    function initializeShareButtons() {
        window
            .jQuery(document)
            .querySelectorAll(".share-button")
            .forEach((button) => {
                button.addEventListener("click", function () {
                    const postId = this.getAttribute("data-post-id")
                    const postElement = this.closest("li")
                    const sharesCountElement = postElement.querySelector(".stats-right .stats-text:first-child")

                    window.jQuery.ajax({
                        url: "/share-post",
                        type: "POST",
                        data: {
                            postId: postId,
                        },
                        success: (response) => {
                            sharesCountElement.textContent = response.sharesCount + " Shares"
                        },
                        error: (xhr) => {
                            alert("Error sharing post: " + xhr.responseText)
                        },
                    })
                })
            })
    }

    // Initialize comment toggles
    function initializeCommentToggles() {
        window
            .jQuery(document)
            .querySelectorAll(".comment-toggle")
            .forEach((toggle) => {
                toggle.addEventListener("click", function () {
                    const commentBox = this.closest(".timeline-body").querySelector(".timeline-comment-box")
                    const commentsContainer = this.closest(".timeline-body").querySelector(".comments-container")

                    if (commentBox.style.display === "none") {
                        commentBox.style.display = "block"
                        commentsContainer.style.display = "block"
                    } else {
                        commentBox.style.display = "none"
                        commentsContainer.style.display = "none"
                    }
                })
            })
    }

    // Initialize all interactive elements
    initializeCommentForms()
    initializeLikeButtons()
    initializeShareButtons()
    initializeCommentToggles()
})
