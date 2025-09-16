'use client'

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { ChevronRight, Star, TrendingUp, Shield, Truck } from 'lucide-react'
import Link from 'next/link'

const heroSlides = [
  {
    title: 'Find Your Perfect Companion',
    subtitle: 'Discover loving pets waiting for their forever home',
    image: 'https://images.unsplash.com/photo-1450778869180-41d0601e046e?w=1920&h=1080&fit=crop',
    cta: 'Browse All Pets',
    link: '/pets',
  },
  {
    title: 'Premium Pet Supplies',
    subtitle: 'Everything your pet needs for a happy, healthy life',
    image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=1920&h=1080&fit=crop',
    cta: 'Shop Now',
    link: '/products',
  },
  {
    title: 'New Arrivals This Week',
    subtitle: 'Meet our newest furry, feathered, and finned friends',
    image: 'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=1920&h=1080&fit=crop',
    cta: 'View New Pets',
    link: '/new-arrivals',
  },
]

const features = [
  {
    icon: Shield,
    title: 'Health Guarantee',
    description: 'All pets come with health certificates',
  },
  {
    icon: Truck,
    title: 'Safe Delivery',
    description: 'Secure and comfortable pet transportation',
  },
  {
    icon: Star,
    title: 'Expert Care',
    description: 'Professional guidance and support',
  },
  {
    icon: TrendingUp,
    title: 'Best Prices',
    description: 'Competitive pricing on all pets and supplies',
  },
]

export default function HeroSection() {
  const [currentSlide, setCurrentSlide] = useState(0)

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % heroSlides.length)
    }, 5000)
    return () => clearInterval(timer)
  }, [])

  return (
    <section className="relative">
      {/* Hero Carousel */}
      <div className="relative h-[600px] overflow-hidden">
        {heroSlides.map((slide, index) => (
          <div
            key={index}
            className={`absolute inset-0 transition-opacity duration-1000 ${
              index === currentSlide ? 'opacity-100' : 'opacity-0'
            }`}
          >
            {/* Background Image */}
            <div
              className="absolute inset-0 bg-cover bg-center"
              style={{
                backgroundImage: `url(${slide.image})`,
              }}
            >
              <div className="absolute inset-0 bg-gradient-to-r from-background/90 to-background/40" />
            </div>

            {/* Content */}
            <div className="relative container mx-auto px-4 h-full flex items-center">
              <div className="max-w-2xl">
                <h1 className="text-5xl md:text-6xl font-bold mb-4 animate-in fade-in slide-in-from-bottom-4 duration-700">
                  {slide.title}
                </h1>
                <p className="text-xl md:text-2xl mb-8 text-muted-foreground animate-in fade-in slide-in-from-bottom-4 duration-700 delay-100">
                  {slide.subtitle}
                </p>
                <div className="flex gap-4 animate-in fade-in slide-in-from-bottom-4 duration-700 delay-200">
                  <Link href={slide.link}>
                    <Button size="lg" className="group">
                      {slide.cta}
                      <ChevronRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
                    </Button>
                  </Link>
                  <Link href="/about">
                    <Button size="lg" variant="outline">
                      Learn More
                    </Button>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        ))}

        {/* Slide Indicators */}
        <div className="absolute bottom-8 left-1/2 -translate-x-1/2 flex gap-2">
          {heroSlides.map((_, index) => (
            <button
              key={index}
              onClick={() => setCurrentSlide(index)}
              className={`h-2 transition-all duration-300 ${
                index === currentSlide ? 'w-8 bg-primary' : 'w-2 bg-primary/30'
              } rounded-full`}
              aria-label={`Go to slide ${index + 1}`}
            />
          ))}
        </div>
      </div>

      {/* Features Bar */}
      <div className="bg-muted/50 border-y">
        <div className="container mx-auto px-4 py-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
            {features.map((feature, index) => (
              <div
                key={index}
                className="flex items-center gap-3 animate-in fade-in slide-in-from-bottom-4 duration-700"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <div className="flex-shrink-0">
                  <feature.icon className="h-8 w-8 text-primary" />
                </div>
                <div>
                  <h3 className="font-semibold">{feature.title}</h3>
                  <p className="text-sm text-muted-foreground">{feature.description}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}
