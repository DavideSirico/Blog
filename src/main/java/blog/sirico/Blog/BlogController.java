package blog.sirico.Blog;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import java.util.*;
import java.time.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogController {

	private Posts posts;
	private XML xml;
	@Value("${xml.path:src/main/resources/xml/posts.xml}")
    private String path;
	/**
     * Initializes the controller.
     * This method is called after the controller is constructed.
     */
	@PostConstruct
	public void init() {
		xml = new XML(path);
		posts = xml.read();
	}

	@GetMapping("/")
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("posts", posts);
		boolean logged = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
		model.addAttribute("logged", logged);
		return "index";
	}

	@GetMapping("/post/{id}")
	public String post(@PathVariable String id, Model model) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		// increment the number of views of the post(id)
		posts.addView(id);
		xml.write(posts);
		model.addAttribute("post", post);
		return "post";
	}

	@GetMapping("/not-found")
	public String notFound() {
		return "not-found";
	}

	// form to create a new post
	@GetMapping("/new-post")
	public String newPost() {
		return "new-post";
	}

	@PostMapping("/new-post")
	public String newPost(@RequestParam String title, @RequestParam String content) {
		// get last post id
		int last_id = xml.getLastId();
		String id = Integer.toString(last_id + 1);
		Post post = new Post(id, title, content, new ArrayList<Comment>(), 0, LocalDate.now());
		posts.addPost(post);
		xml.write(posts);
		// redirect to the home page
		return "redirect:/";
	}
	
	@PostMapping("/post/{id}/add-comment")
	public String addComment(@PathVariable String id, @RequestParam String content, @RequestParam String author) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		Comment comment = new Comment(author, content, LocalDate.now());
		posts.addComment(id, comment);
		xml.write(posts);
		return "redirect:/post/" + id;
	}

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        return "login";
    }

	@PostMapping("/logout")
	public String logoutPost(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

	@PostMapping("/remove")
	public String postMethodName(@RequestParam String id) {
		posts.removePost(id);
		xml.write(posts);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		model.addAttribute("post", post);
		return "edit";
	}

	@PostMapping("/post/{id}/edit-title")
	public String editTitle(@PathVariable String id, @RequestParam String title) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		posts.editTitle(id, title);
		xml.write(posts);
		return "redirect:/edit/" + id;
	}

	@PostMapping("/post/{id}/edit-content")
	public String editContent(@PathVariable String id, @RequestParam String content) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		posts.editContent(id, content);
		xml.write(posts);
		return "redirect:/edit/" + id;
	}

	// search
	@GetMapping("/search")
	public String search(@RequestParam String q, Model model) {
		List<Post> result = new ArrayList<Post>();
		for(Post post : posts) {
			if(post.getTitle().toLowerCase().contains(q.toLowerCase()) || post.getContent().toLowerCase().contains(q.toLowerCase())) {
				result.add(post);
			}
		}
		model.addAttribute("posts", result);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean logged = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");

		model.addAttribute("logged", logged);
		return "index";
	}

	@GetMapping("/error")
	public String error() {
		return "error";
	}


}