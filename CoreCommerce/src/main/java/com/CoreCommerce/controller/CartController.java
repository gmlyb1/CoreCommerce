package com.CoreCommerce.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.CoreCommerce.domain.Cart;
import com.CoreCommerce.domain.CartItem;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.CartRepository;
import com.CoreCommerce.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/cart")
public class CartController {

  	private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepo; // Cart 테이블용 Repository

    public CartController(CartRepository cartRepository,
                          ProductRepository productRepository,
                          CartRepository cartRepo) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartRepo = cartRepo;
    }

//    @GetMapping("/list")
//    public String listCart(HttpSession session, Model model) {
//
//        Member loginUser = (Member) session.getAttribute("loginUser");
//        
//        Long userId = loginUser.getId();
//
//        Cart cart = cartRepository.findCartByUserId(userId);
//
//        if (cart == null) {
//            model.addAttribute("items", new ArrayList<>());
//            model.addAttribute("totalPrice", 0);
//            return "cart/list";
//        }
//
//        List<CartItem> cartItems = cartRepository.findByCartId(cart.getId());
//
//        int totalPrice = cartItems.stream()
//                .mapToInt(item -> item.getPrice() * item.getQuantity())
//                .sum();
//
//        model.addAttribute("items", cartItems);
//        model.addAttribute("cartId", cart.getId());
//        model.addAttribute("totalPrice", totalPrice);
//
//        return "cart/list";
//    }
    
    @GetMapping("/list")
    public String listCart(@RequestParam(defaultValue = "1") int page,
                           HttpSession session,
                           Model model) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Long userId = loginUser.getId();

        int size = 5; // 한 페이지에 보여줄 개수
        int offset = (page - 1) * size;

        Cart cart = cartRepository.findCartByUserId(userId);

        List<CartItem> cartItems = new ArrayList<>();
        int totalCount = 0;

        if (cart != null) {

            cartItems = cartRepository.findByCartIdPaging(  cart.getId(), offset, size);

            totalCount = cartRepository.countByCartId(cart.getId());
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("items", cartItems);
        model.addAttribute("totalPrice",
                cartItems.stream()
                        .mapToInt(i -> i.getPrice() * i.getQuantity())
                        .sum());

        model.addAttribute("pagination", new Pagination(page, size, totalCount));

        model.addAttribute("cartId", cart != null ? cart.getId() : null);

        return "cart/list";
    }

    // 장바구니 담기
    @PostMapping("/add")
    public String addCart(@RequestParam Long productId,
                          @RequestParam(defaultValue = "1") int quantity,
                          HttpSession session) {

        // 1️⃣ 로그인 사용자 확인
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Long userId = loginUser.getId();

        // 2️⃣ 사용자 장바구니 조회 / 없으면 생성
        Cart cart = cartRepository.findCartByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);   // FK 컬럼에 맞게 memberId 사용
            cartRepository.createCart(cart); // insert 후 cart.id 자동 세팅
        }

        // 3️⃣ 상품 조회
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new RuntimeException("상품이 존재하지 않습니다.");
        }

        // 4️⃣ CartItem 중복 처리
        CartItem existingItem = cartRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (existingItem != null) {
            // 이미 존재하면 수량 증가
            int newQuantity = existingItem.getQuantity() + quantity;
            cartRepository.updateQuantity(existingItem.getId(), newQuantity);
        } else {
            // 없으면 새로 추가
            cartRepository.add(cart.getId(), product.getId(), quantity);
        }

        return "redirect:/cart/list";
    }

    @PostMapping("/delete")
    public String deleteCart(@RequestParam Long itemId) {
        cartRepository.delete(itemId);
        return "redirect:/cart/list";
    }
    
    @PostMapping("/delete-selected")
    @ResponseBody
    public void deleteSelected(@RequestBody List<Long> cartItemIds,
                               @SessionAttribute("loginUser") Member member) {

    	cartRepository.deleteByIdsAndMemberId(cartItemIds, member.getId());
    }
}
