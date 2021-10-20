package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.service.CartService;
import com.codecool.shop.service.ProductService;
import org.json.JSONException;
import org.json.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cartId = req.getParameter("cart_id");

        cartId = cartId == null ? "0" : cartId;

        ProductDao productDao = ProductDaoMem.getInstance();
        CartDao cartDao = CartDaoMem.getInstance();
        CartService cartService = new CartService(cartDao, productDao);

        List<JSONObject> itemsInCart = cartService.getCartContent(cartId);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        BigDecimal totalPrice = new BigDecimal(0);

        for (JSONObject item: itemsInCart) {
            try {
                totalPrice = totalPrice.add(BigDecimal.valueOf(item.getDouble("price")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        context.setVariable("totalPrice", totalPrice);

        engine.process("payment/payment.html", context, resp.getWriter());
    }

}
