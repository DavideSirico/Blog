package blog.sirico.Blog;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.time.*;

@Controller
public class BlogController {

	private Posts posts;

	public BlogController() {
		posts = new Posts();
		posts.parseXML("src/main/resources/xml/posts.xml");
	}

	@GetMapping("/")
	// use index.html template
	public String index(Model model) {
		model.addAttribute("posts", posts);
		return "index";
	}

	@GetMapping("/post/{id}")
	public String post(@PathVariable String id, Model model) {
		Post post = posts.getPost(id);
		if(post == null) {
			return "redirect:/not-found";
		}
		model.addAttribute("post", post);
		return "post";
	}
	@GetMapping("/not-found")
	public String notFound(Model model) {

		return "not-found";
	}

	@GetMapping("/new-post")
	public String newPost(Model model) {
		return "new-post";
	}

	@PostMapping("/new-post")
	public String newPost(@RequestParam String title, @RequestParam String content, @RequestParam String category, @RequestParam String tags, Model model) {
		Post post = new Post("0", title, content, category, new ArrayList<String>(), new ArrayList<Comment>(), 0, LocalDate.now());
		posts.addPost(post);
		return "redirect:/";
	}
	
	// @GetMapping("/post/{id}/add-comment")


}