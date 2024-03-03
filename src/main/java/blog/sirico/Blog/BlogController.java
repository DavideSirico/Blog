package blog.sirico.Blog;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.time.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class BlogController {

	private Posts posts;
	private User user;
	private boolean logged = false;

	public BlogController() {
		user = new User("admin", "admin");
		posts = new Posts("src/main/resources/xml/posts.xml");
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("posts", posts);
		model.addAttribute("logged", logged);
		for(Post post : posts.getPosts()){
			for(String tag : post.getTags()){
				System.out.println(tag);
			}
		}
		return "index";
	}
	@GetMapping("/index-old")
	public String indexOld(Model model) {
		model.addAttribute("posts", posts);
		for(Post post : posts.getPosts()){
			for(String tag : post.getTags()){
				System.out.println(tag);
			}
		}
		return "index.old";
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
		return "new-post";
	}

	@PostMapping("/new-post")
	public String newPost(@RequestParam String title, @RequestParam String content, @RequestParam String category, @RequestParam String tags, Model model) {
		// get last post id
		String id = Integer.toString(posts.getPosts().size());
		ArrayList<String> tagsList = new ArrayList<String>(Arrays.asList(tags.split(",")));
		Post post = new Post(id, title, content, category, tagsList, new ArrayList<Comment>(), 0, LocalDate.now());
		posts.addPost(post);
		// redirect to the home page
		return "redirect:/";
	}
	
	@PostMapping("/post/{id}/add-comment")
	public String addComment(@PathVariable String id, @RequestParam String content, @RequestParam String Author, @RequestParam String email, Model model) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		Comment comment = new Comment(content, Author, email, LocalDate.now());
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
			logged = true;
			return "redirect:/";
		}
		return "redirect:/login?error=username o password sbagliato";
	}

	@GetMapping("/logout")
	public String logout() {
		logged = false;
		return "redirect:/";
	}

	@PostMapping("/remove")
	public String postMethodName(@RequestParam String id) {
		posts.removePost(id);
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

}