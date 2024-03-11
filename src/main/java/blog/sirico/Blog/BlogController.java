package blog.sirico.Blog;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.time.*;


@Controller
public class BlogController {

	private Posts posts;
	private User user;
	private boolean logged = false;

	@Value("${path}")
	private String path;

	public BlogController() {
		user = new User("admin", "admin");
		System.out.println(path);
		posts = new Posts("src/main/resources/xml/posts.xml");
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("posts", posts);
		model.addAttribute("logged", this.logged);
		return "index";
	}

	@GetMapping("/post/{id}")
	public String post(@PathVariable String id, Model model) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		posts.addView(id);
		model.addAttribute("post", post);
		return "post";
	}

	@GetMapping("/not-found")
	public String notFound(Model model) {
		return "not-found";
	}

	// form to create a new post
	@GetMapping("/new-post")
	public String newPost(Model model) {
		if(!this.logged) {
			return "redirect:/login";
		}
		return "new-post";
	}

	@PostMapping("/new-post")
	public String newPost(@RequestParam String title, @RequestParam String content, Model model) {
		// get last post id
		int last_id = posts.getLastId();
		String id = Integer.toString(last_id + 1);
		Post post = new Post(id, title, content, new ArrayList<Comment>(), 0, LocalDate.now());
		posts.addPost(post);
		// redirect to the home page
		return "redirect:/";
	}
	
	@PostMapping("/post/{id}/add-comment")
	public String addComment(@PathVariable String id, @RequestParam String content, @RequestParam String author, Model model) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		Comment comment = new Comment(author, content, LocalDate.now());
		posts.addComment(id, comment);
		model.addAttribute("post", post);
		return "redirect:/post/" + id;
	}

	@GetMapping("/login")
	public String getMethodName(@RequestParam(defaultValue = "") String error, Model model) {
		model.addAttribute("errorMessage", error);
		return "login";
	}

	@PostMapping("/login") 
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
		System.out.println(username);
		System.out.println(password);
		if(user.getUsername().equals(username) && user.checkPassword(password)) {
			this.logged = true;
			return "redirect:/";
		}
		return "redirect:/login?error=username o password sbagliato";
	}

	@GetMapping("/logout")
	public String logout() {
		this.logged = false;
		return "redirect:/";
	}

	@PostMapping("/remove")
	public String postMethodName(@RequestParam String id) {
		if(!this.logged) {
			return "redirect:/login";
		}
		posts.removePost(id);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		if(!this.logged) {
			return "redirect:/login";
		}
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		model.addAttribute("post", post);
		return "edit";
	}

	@PostMapping("/post/{id}/edit-title")
	public String editTitle(@PathVariable String id, @RequestParam String title, Model model) {
		if(!this.logged) {
			return "redirect:/login";
		}
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		posts.editTitle(id, title);
		return "redirect:/post/" + id;
	}

	@PostMapping("/post/{id}/edit-content")
	public String editContent(@PathVariable String id, @RequestParam String content, Model model) {
		if(!this.logged) {
			return "redirect:/login";
		}
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		posts.editContent(id, content);
		return "redirect:/post/" + id;
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
		model.addAttribute("logged", this.logged);
		return "index";
	}

}