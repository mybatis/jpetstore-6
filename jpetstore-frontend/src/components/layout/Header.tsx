'use client'

import Link from 'next/link'
import { useState } from 'react'
import { ShoppingCart, User, Menu, Search, Heart, PawPrint } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
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
  const [cartCount, setCartCount] = useState(3)
  const [isSearchOpen, setIsSearchOpen] = useState(false)

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
            <Sheet>
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
              <SheetContent>
                <SheetHeader>
                  <SheetTitle>Shopping Cart</SheetTitle>
                  <SheetDescription>
                    You have {cartCount} items in your cart
                  </SheetDescription>
                </SheetHeader>
                <div className="mt-8">
                  {/* Cart items would go here */}
                  <p className="text-muted-foreground">Your cart items will appear here</p>
                </div>
              </SheetContent>
            </Sheet>

            {/* User Account */}
            <Button variant="ghost" size="icon">
              <User className="h-5 w-5" />
            </Button>

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
    </header>
  )
}
