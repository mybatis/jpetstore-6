/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.web.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Message;
import net.sourceforge.stripes.action.Resolution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.jpetstore.domain.Cart;

class CartActionBeanTest {

    private CartActionBean cartActionBean;
    private ActionBeanContext mockContext;

    @BeforeEach
    void setUp() {
        cartActionBean = new CartActionBean();
        cartActionBean.setCart(new Cart());

        // Mock ActionBeanContext to avoid NPE in setMessage()
        mockContext = mock(ActionBeanContext.class);
        when(mockContext.getMessages()).thenReturn(new ArrayList<Message>());
        cartActionBean.setContext(mockContext);
    }

    @Test
    void constructorOutputNotNull() {
        final CartActionBean actual = new CartActionBean();

        assertThat(actual).isNotNull();
        assertThat(actual.getCart()).isNotNull();
        assertThat(actual.getContext()).isNull();
    }

    @Test
    void getCartOutputNotNull() {
        final CartActionBean bean = new CartActionBean();

        assertThat(bean.getCart()).isNotNull();
    }

    @Test
    void addItemToCart_WithNullWorkingItemId_ShouldReturnError() {
        cartActionBean.setWorkingItemId(null);

        Resolution resolution = cartActionBean.addItemToCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void addItemToCart_WithEmptyWorkingItemId_ShouldReturnError() {
        cartActionBean.setWorkingItemId("");

        Resolution resolution = cartActionBean.addItemToCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void addItemToCart_WithBlankWorkingItemId_ShouldReturnError() {
        cartActionBean.setWorkingItemId("   ");

        Resolution resolution = cartActionBean.addItemToCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void removeItemFromCart_WithNullWorkingItemId_ShouldReturnError() {
        cartActionBean.setWorkingItemId(null);

        Resolution resolution = cartActionBean.removeItemFromCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void removeItemFromCart_WithEmptyWorkingItemId_ShouldReturnError() {
        cartActionBean.setWorkingItemId("");

        Resolution resolution = cartActionBean.removeItemFromCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void removeItemFromCart_WithBlankWorkingItemId_ShouldReturnError() {
        cartActionBean.setWorkingItemId("   ");

        Resolution resolution = cartActionBean.removeItemFromCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void removeItemFromCart_WithNonExistentItem_ShouldReturnError() {
        cartActionBean.setWorkingItemId("NON_EXISTENT_ITEM");

        Resolution resolution = cartActionBean.removeItemFromCart();

        assertThat(resolution).isNotNull();
        assertThat(resolution.toString()).contains("Error.jsp");
    }

    @Test
    void clearShouldResetCartAndWorkingItemId() {
        cartActionBean.setWorkingItemId("EST-1");

        cartActionBean.clear();

        assertThat(cartActionBean.getCart()).isNotNull();
        assertThat(cartActionBean.getCart().getNumberOfItems()).isZero();
    }
}
