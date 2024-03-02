package blog.sirico.Blog;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.time.*;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BlogController {

	private Posts posts;

	public BlogController() {
		posts = new Posts("src/main/resources/xml/posts.xml");
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("posts", posts);
		model.addAttribute("logged", true);
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
	public String getMethodName(Model model) {
		return "login";
	}
	

	

}