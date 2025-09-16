'use client'

import { useState } from 'react'
import Image from 'next/image'
import Link from 'next/link'
import { Heart, ShoppingCart, Star, Eye, Info } from 'lucide-react'
import { Card, CardContent, CardFooter } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'

interface PetCardProps {
  id: string
  name: string
  breed: string
  category: string
  price: number
  image: string
  age?: string
  gender?: 'Male' | 'Female'
  status?: 'Available' | 'Reserved' | 'Sold'
  rating?: number
  description?: string
  traits?: string[]
  isNew?: boolean
  isFeatured?: boolean
}

export default function PetCard({
  id,
  name,
  breed,
  category,
  price,
  image,
  age = 'Young',
  gender = 'Male',
  status = 'Available',
  rating = 4.5,
  description = 'A wonderful companion waiting for a loving home.',
  traits = ['Friendly', 'Playful', 'Vaccinated'],
  isNew = false,
  isFeatured = false,
}: PetCardProps) {
  const [isLiked, setIsLiked] = useState(false)
  const [imageLoading, setImageLoading] = useState(true)

  const handleAddToCart = (e: React.MouseEvent) => {
    e.preventDefault()
    // Add to cart logic here
    console.log('Added to cart:', { id, name, price })
  }

  const handleToggleLike = (e: React.MouseEvent) => {
    e.preventDefault()
    setIsLiked(!isLiked)
  }

  return (
    <Card className="group relative overflow-hidden transition-all duration-300 hover:shadow-xl">
      {/* Badges */}
      <div className="absolute top-2 left-2 z-10 flex flex-col gap-2">
        {isNew && (
          <Badge className="bg-green-500 text-white">New</Badge>
        )}
        {isFeatured && (
          <Badge className="bg-yellow-500 text-white">Featured</Badge>
        )}
        {status === 'Reserved' && (
          <Badge variant="secondary">Reserved</Badge>
        )}
      </div>

      {/* Like Button */}
      <button
        onClick={handleToggleLike}
        className="absolute top-2 right-2 z-10 p-2 rounded-full bg-background/80 backdrop-blur-sm transition-all hover:bg-background"
      >
        <Heart
          className={`h-5 w-5 transition-colors ${
            isLiked ? 'fill-red-500 text-red-500' : 'text-muted-foreground'
          }`}
        />
      </button>

      {/* Image Container */}
      <Link href={`/pet/${id}`}>
        <div className="relative aspect-square overflow-hidden bg-muted">
          {imageLoading && (
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary border-t-transparent" />
            </div>
          )}
          <img
            src={image}
            alt={name}
            className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-110"
            onLoad={() => setImageLoading(false)}
          />
          
          {/* Quick View Overlay */}
          <div className="absolute inset-0 bg-gradient-to-t from-background/80 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-end justify-center pb-4">
            <Dialog>
              <DialogTrigger asChild>
                <Button size="sm" variant="secondary" className="gap-2">
                  <Eye className="h-4 w-4" />
                  Quick View
                </Button>
              </DialogTrigger>
              <DialogContent className="max-w-2xl">
                <DialogHeader>
                  <DialogTitle>{name} - {breed}</DialogTitle>
                  <DialogDescription>
                    {category} • {age} • {gender}
                  </DialogDescription>
                </DialogHeader>
                <Tabs defaultValue="details" className="mt-4">
                  <TabsList className="grid w-full grid-cols-3">
                    <TabsTrigger value="details">Details</TabsTrigger>
                    <TabsTrigger value="traits">Traits</TabsTrigger>
                    <TabsTrigger value="care">Care Info</TabsTrigger>
                  </TabsList>
                  <TabsContent value="details" className="space-y-4">
                    <div className="grid grid-cols-2 gap-4">
                      <img
                        src={image}
                        alt={name}
                        className="rounded-lg w-full h-64 object-cover"
                      />
                      <div className="space-y-2">
                        <p className="text-sm text-muted-foreground">{description}</p>
                        <div className="space-y-1">
                          <p><strong>Price:</strong> ${price.toFixed(2)}</p>
                          <p><strong>Status:</strong> {status}</p>
                          <p><strong>Age:</strong> {age}</p>
                          <p><strong>Gender:</strong> {gender}</p>
                        </div>
                      </div>
                    </div>
                  </TabsContent>
                  <TabsContent value="traits" className="space-y-4">
                    <div className="flex flex-wrap gap-2">
                      {traits.map((trait) => (
                        <Badge key={trait} variant="secondary">
                          {trait}
                        </Badge>
                      ))}
                    </div>
                  </TabsContent>
                  <TabsContent value="care" className="space-y-4">
                    <p className="text-sm text-muted-foreground">
                      Proper care instructions and requirements for {name} will be provided upon adoption.
                      Our team will guide you through everything you need to know.
                    </p>
                  </TabsContent>
                </Tabs>
              </DialogContent>
            </Dialog>
          </div>
        </div>
      </Link>

      <CardContent className="p-4">
        {/* Pet Info */}
        <Link href={`/pet/${id}`}>
          <h3 className="font-semibold text-lg hover:text-primary transition-colors">
            {name}
          </h3>
        </Link>
        <p className="text-sm text-muted-foreground">{breed}</p>
        
        {/* Rating */}
        <div className="flex items-center gap-1 mt-2">
          <div className="flex">
            {[...Array(5)].map((_, i) => (
              <Star
                key={i}
                className={`h-4 w-4 ${
                  i < Math.floor(rating)
                    ? 'fill-yellow-400 text-yellow-400'
                    : 'text-muted-foreground'
                }`}
              />
            ))}
          </div>
          <span className="text-sm text-muted-foreground">({rating})</span>
        </div>

        {/* Traits */}
        <div className="flex gap-1 mt-2 flex-wrap">
          {traits.slice(0, 2).map((trait) => (
            <Badge key={trait} variant="outline" className="text-xs">
              {trait}
            </Badge>
          ))}
          {traits.length > 2 && (
            <Badge variant="outline" className="text-xs">
              +{traits.length - 2}
            </Badge>
          )}
        </div>
      </CardContent>

      <CardFooter className="p-4 pt-0 flex items-center justify-between">
        <div>
          <p className="text-2xl font-bold">${price.toFixed(2)}</p>
          <p className="text-xs text-muted-foreground">{status}</p>
        </div>
        <Button
          size="sm"
          onClick={handleAddToCart}
          disabled={status !== 'Available'}
          className="gap-2"
        >
          <ShoppingCart className="h-4 w-4" />
          Add to Cart
        </Button>
      </CardFooter>
    </Card>
  )
}
