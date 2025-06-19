function addToCart(productId, productName, image, quantity, price) {
      const item = {
          productId,
          productName,
          image,
          quantity,
          price
      };

      fetch('/api/cart/add', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(item)
      })
      .then(response => {
          if (response.ok) {
              alert("Đã thêm vào giỏ hàng!");

              // Gọi lại API giỏ hàng để cập nhật số lượng hiển thị
              return fetch('/api/cart');
          } else {
              alert("Lỗi khi thêm vào giỏ hàng.");
              throw new Error("Add to cart failed");
          }
      })
      .then(res => res.json())
      .then(cartItems => {
          let count = cartItems.reduce((sum, item) => sum + item.quantity, 0);
          const cartBadge = document.getElementById("cart-count");
          if (cartBadge) {
              cartBadge.innerText = count;
              cartBadge.style.display = count > 0 ? 'inline-block' : 'none';
          }
      })
      .catch(error => {
          console.error("Lỗi:", error);
          alert("Đã xảy ra lỗi.");
      });
  }
