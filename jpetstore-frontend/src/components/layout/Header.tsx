'use client'

import Link from 'next/link'
import { useState, useEffect } from 'react'
import { ShoppingCart, User, Menu, Search, Heart, PawPrint, LogOut, Trash2, Plus, Minus } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useAuth } from '@/contexts/AuthContext'
import AuthModal from '@/components/auth/AuthModal'
import { cartAPI, type Cart, type CartItem } from '@/lib/api'
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
} from '@/components/ui/navigation-menu'
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from '@/components/ui/sheet'
import { Badge } from '@/components/ui/badge'

const categories = [
  {
    name: 'Dogs',
    icon: '🐕',
    subcategories: ['Bulldog', 'Poodle', 'Dalmation', 'Golden Retriever', 'Labrador'],
  },
  {
    name: 'Cats',
    icon: '🐈',
    subcategories: ['Manx', 'Persian', 'Siamese', 'Maine Coon', 'British Shorthair'],
  },
  {
    name: 'Birds',
    icon: '🦜',
    subcategories: ['Amazon Parrot', 'Finch', 'Cockatiel', 'Budgie'],
  },
  {
    name: 'Fish',
    icon: '🐠',
    subcategories: ['Koi', 'Goldfish', 'Angelfish', 'Tiger Shark', 'Betta'],
  },
  {
    name: 'Reptiles',
    icon: '🦎',
    subcategories: ['Iguana', 'Rattlesnake', 'Python', 'Gecko', 'Chameleon'],
  },
]

export default function Header() {
  const { user, logout } = useAuth()
  const [cartCount, setCartCount] = useState(0)
  const [cart, setCart] = useState<Cart | null>(null)
  const [isCartOpen, setIsCartOpen] = useState(false)
  const [isSearchOpen, setIsSearchOpen] = useState(false)
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false)
  const [isLoadingCart, setIsLoadingCart] = useState(false)
  
  // Fetch cart count on mount and when user changes
  useEffect(() => {
    if (user) {
      fetchCartCount()
    } else {
      setCartCount(0)
    }
  }, [user])
  
  // Listen for cart update events
  useEffect(() => {
    const handleCartUpdate = () => {
      fetchCartCount()
      if (isCartOpen) {
        fetchCart()
      }
    }
    
    window.addEventListener('cartUpdated', handleCartUpdate)
    
    return () => {
      window.removeEventListener('cartUpdated', handleCartUpdate)
    }
  }, [isCartOpen])
  
  // Fetch full cart when cart sheet is opened
  useEffect(() => {
    if (isCartOpen) {
      fetchCart()
    }
  }, [isCartOpen])
  
  const fetchCartCount = async () => {
    try {
      const response = await cartAPI.getCartCount()
      setCartCount(response.count)
    } catch (error) {
      console.error('Failed to fetch cart count:', error)
    }
  }
  
  const fetchCart = async () => {
    setIsLoadingCart(true)
    try {
      const response = await cartAPI.getCart()
      setCart(response)
    } catch (error) {
      console.error('Failed to fetch cart:', error)
    } finally {
      setIsLoadingCart(false)
    }
  }
  
  const updateCartItem = async (itemId: string, quantity: number) => {
    try {
      await cartAPI.updateCartItem(itemId, quantity)
      fetchCart()
      fetchCartCount()
    } catch (error) {
      console.error('Failed to update cart item:', error)
    }
  }
  
  const removeFromCart = async (itemId: string) => {
    try {
      await cartAPI.removeFromCart(itemId)
      fetchCart()
      fetchCartCount()
    } catch (error) {
      console.error('Failed to remove item from cart:', error)
    }
  }
  
  const clearCart = async () => {
    try {
      await cartAPI.clearCart()
      setCart(null)
      setCartCount(0)
    } catch (error) {
      console.error('Failed to clear cart:', error)
    }
  }
  
  const handleLogout = async () => {
    try {
      await logout()
      setCartCount(0)
    } catch (error) {
      console.error('Logout failed:', error)
    }
  }

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container mx-auto px-4">
        {/* Top Bar */}
        <div className="flex h-16 items-center justify-between">
          {/* Logo */}
          <Link href="/" className="flex items-center space-x-2">
            <PawPrint className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold bg-gradient-to-r from-primary to-primary/60 bg-clip-text text-transparent">
              PetStore
            </span>
          </Link>

          {/* Desktop Navigation */}
          <NavigationMenu className="hidden lg:flex">
            <NavigationMenuList>
              {categories.map((category) => (
                <NavigationMenuItem key={category.name}>
                  <NavigationMenuTrigger className="h-10">
                    <span className="mr-2">{category.icon}</span>
                    {category.name}
                  </NavigationMenuTrigger>
                  <NavigationMenuContent>
                    <ul className="grid w-[400px] gap-3 p-4 md:w-[500px] md:grid-cols-2">
                      {category.subcategories.map((sub) => (
                        <li key={sub}>
                          <NavigationMenuLink asChild>
                            <Link
                              href={`/category/${category.name.toLowerCase()}/${sub.toLowerCase().replace(' ', '-')}`}
                              className="block select-none space-y-1 rounded-md p-3 leading-none no-underline outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground"
                            >
                              <div className="text-sm font-medium leading-none">{sub}</div>
                              <p className="line-clamp-2 text-sm leading-snug text-muted-foreground">
                                Browse our {sub} collection
                              </p>
                            </Link>
                          </NavigationMenuLink>
                        </li>
                      ))}
                    </ul>
                  </NavigationMenuContent>
                </NavigationMenuItem>
              ))}
            </NavigationMenuList>
          </NavigationMenu>

          {/* Search Bar */}
          <div className="hidden lg:flex flex-1 max-w-md mx-8">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                type="search"
                placeholder="Search for pets, products..."
                className="pl-10 pr-4"
              />
            </div>
          </div>

          {/* Right Actions */}
          <div className="flex items-center space-x-4">
            {/* Mobile Search Toggle */}
            <Button
              variant="ghost"
              size="icon"
              className="lg:hidden"
              onClick={() => setIsSearchOpen(!isSearchOpen)}
            >
              <Search className="h-5 w-5" />
            </Button>

            {/* Wishlist */}
            <Button variant="ghost" size="icon" className="relative">
              <Heart className="h-5 w-5" />
              <Badge className="absolute -top-1 -right-1 h-5 w-5 rounded-full p-0 flex items-center justify-center">
                2
              </Badge>
            </Button>

            {/* Cart */}
            <Sheet open={isCartOpen} onOpenChange={setIsCartOpen}>
              <SheetTrigger asChild>
                <Button variant="ghost" size="icon" className="relative">
                  <ShoppingCart className="h-5 w-5" />
                  {cartCount > 0 && (
                    <Badge className="absolute -top-1 -right-1 h-5 w-5 rounded-full p-0 flex items-center justify-center">
                      {cartCount}
                    </Badge>
                  )}
                </Button>
              </SheetTrigger>
              <SheetContent className="w-full sm:max-w-lg">
                <SheetHeader>
                  <SheetTitle>Shopping Cart</SheetTitle>
                  <SheetDescription>
                    {cartCount > 0 ? `You have ${cartCount} item${cartCount > 1 ? 's' : ''} in your cart` : 'Your cart is empty'}
                  </SheetDescription>
                </SheetHeader>
                
                <div className="mt-8 flex-1 overflow-y-auto">
                  {isLoadingCart ? (
                    <div className="flex justify-center py-8">
                      <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary border-t-transparent" />
                    </div>
                  ) : cart && cart.items && cart.items.length > 0 ? (
                    <div className="space-y-4">
                      {cart.items.map((item: CartItem) => (
                        <div key={item.itemId} className="flex gap-4 rounded-lg border p-4">
                          {item.imageUrl && (
                            <img
                              src={item.imageUrl}
                              alt={item.name}
                              className="h-20 w-20 rounded-md object-cover"
                            />
                          )}
                          <div className="flex-1">
                            <h4 className="font-semibold">{item.name}</h4>
                            {item.breed && (
                              <p className="text-sm text-muted-foreground">{item.breed}</p>
                            )}
                            <p className="text-sm font-medium">${item.price.toFixed(2)}</p>
                          </div>
                          <div className="flex items-center gap-2">
                            <Button
                              size="icon"
                              variant="outline"
                              className="h-8 w-8"
                              onClick={() => updateCartItem(item.itemId, Math.max(1, item.quantity - 1))}
                            >
                              <Minus className="h-4 w-4" />
                            </Button>
                            <span className="w-8 text-center">{item.quantity}</span>
                            <Button
                              size="icon"
                              variant="outline"
                              className="h-8 w-8"
                              onClick={() => updateCartItem(item.itemId, item.quantity + 1)}
                            >
                              <Plus className="h-4 w-4" />
                            </Button>
                            <Button
                              size="icon"
                              variant="ghost"
                              className="h-8 w-8 text-destructive"
                              onClick={() => removeFromCart(item.itemId)}
                            >
                              <Trash2 className="h-4 w-4" />
                            </Button>
                          </div>
                        </div>
                      ))}
                      
                      {/* Cart Summary */}
                      <div className="border-t pt-4 space-y-2">
                        <div className="flex justify-between text-sm">
                          <span>Subtotal</span>
                          <span>${cart.subtotal.toFixed(2)}</span>
                        </div>
                        <div className="flex justify-between text-sm">
                          <span>Tax</span>
                          <span>${cart.tax.toFixed(2)}</span>
                        </div>
                        <div className="flex justify-between font-semibold text-lg">
                          <span>Total</span>
                          <span>${cart.total.toFixed(2)}</span>
                        </div>
                      </div>
                      
                      {/* Cart Actions */}
                      <div className="space-y-2 pt-4">
                        <Button className="w-full" size="lg">
                          Proceed to Checkout
                        </Button>
                        <Button 
                          variant="outline" 
                          className="w-full"
                          onClick={clearCart}
                        >
                          Clear Cart
                        </Button>
                      </div>
                    </div>
                  ) : (
                    <div className="text-center py-8">
                      <ShoppingCart className="mx-auto h-12 w-12 text-muted-foreground mb-4" />
                      <p className="text-muted-foreground">Your cart is empty</p>
                      <Button 
                        variant="outline" 
                        className="mt-4"
                        onClick={() => setIsCartOpen(false)}
                      >
                        Continue Shopping
                      </Button>
                    </div>
                  )}
                </div>
              </SheetContent>
            </Sheet>

            {/* User Account */}
            {user ? (
              <div className="flex items-center gap-2">
                <span className="text-sm font-medium hidden md:inline">
                  Hi, {user.firstName || user.username}
                </span>
                <Button variant="ghost" size="icon" onClick={handleLogout} title="Logout">
                  <LogOut className="h-5 w-5" />
                </Button>
              </div>
            ) : (
              <Button variant="ghost" size="icon" onClick={() => setIsAuthModalOpen(true)}>
                <User className="h-5 w-5" />
              </Button>
            )}

            {/* Mobile Menu */}
            <Sheet>
              <SheetTrigger asChild>
                <Button variant="ghost" size="icon" className="lg:hidden">
                  <Menu className="h-5 w-5" />
                </Button>
              </SheetTrigger>
              <SheetContent side="left">
                <SheetHeader>
                  <SheetTitle>Menu</SheetTitle>
                </SheetHeader>
                <nav className="mt-8">
                  {categories.map((category) => (
                    <div key={category.name} className="mb-4">
                      <h3 className="font-semibold mb-2 flex items-center">
                        <span className="mr-2">{category.icon}</span>
                        {category.name}
                      </h3>
                      <ul className="ml-6 space-y-2">
                        {category.subcategories.map((sub) => (
                          <li key={sub}>
                            <Link
                              href={`/category/${category.name.toLowerCase()}/${sub.toLowerCase().replace(' ', '-')}`}
                              className="text-sm text-muted-foreground hover:text-foreground"
                            >
                              {sub}
                            </Link>
                          </li>
                        ))}
                      </ul>
                    </div>
                  ))}
                </nav>
              </SheetContent>
            </Sheet>
          </div>
        </div>

        {/* Mobile Search Bar */}
        {isSearchOpen && (
          <div className="lg:hidden pb-4">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                type="search"
                placeholder="Search for pets, products..."
                className="pl-10 pr-4"
              />
            </div>
          </div>
        )}
      </div>
      
      {/* Auth Modal */}
      <AuthModal isOpen={isAuthModalOpen} onClose={() => setIsAuthModalOpen(false)} />
    </header>
  )
}
