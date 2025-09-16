'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { ArrowRight, Sparkles, Loader2 } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import PetCard from '@/components/products/PetCard'
import { catalogAPI } from '@/lib/api'

// We'll still use mock data as fallback since the backend might not have all the detailed data
const mockPets = {
  dogs: [
    {
      id: 'dog-1',
      name: 'Max',
      breed: 'Golden Retriever',
      category: 'Dogs',
      price: 899.99,
      image: 'https://images.unsplash.com/photo-1552053831-71594a27632d?w=500&h=500&fit=crop',
      age: '2 years',
      gender: 'Male' as const,
      traits: ['Friendly', 'Trained', 'Vaccinated', 'Good with kids'],
      isNew: true,
      isFeatured: true,
    },
    {
      id: 'dog-2',
      name: 'Bella',
      breed: 'French Bulldog',
      category: 'Dogs',
      price: 1299.99,
      image: 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=500&h=500&fit=crop',
      age: '1 year',
      gender: 'Female' as const,
      traits: ['Playful', 'Apartment-friendly', 'Vaccinated'],
    },
    {
      id: 'dog-3',
      name: 'Charlie',
      breed: 'Labrador',
      category: 'Dogs',
      price: 799.99,
      image: 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=500&h=500&fit=crop',
      age: '3 years',
      gender: 'Male' as const,
      traits: ['Energetic', 'Trained', 'Loyal'],
      isFeatured: true,
    },
    {
      id: 'dog-4',
      name: 'Luna',
      breed: 'Husky',
      category: 'Dogs',
      price: 999.99,
      image: 'https://images.unsplash.com/photo-1605568427561-40dd23c2acea?w=500&h=500&fit=crop',
      age: '2 years',
      gender: 'Female' as const,
      traits: ['Active', 'Beautiful coat', 'Needs exercise'],
      isNew: true,
    },
  ],
  cats: [
    {
      id: 'cat-1',
      name: 'Whiskers',
      breed: 'Persian',
      category: 'Cats',
      price: 599.99,
      image: 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=500&h=500&fit=crop',
      age: '1 year',
      gender: 'Female' as const,
      traits: ['Calm', 'Indoor', 'Groomed'],
      isFeatured: true,
    },
    {
      id: 'cat-2',
      name: 'Shadow',
      breed: 'Maine Coon',
      category: 'Cats',
      price: 799.99,
      image: 'https://images.unsplash.com/photo-1513360371669-4adf3dd7dff8?w=500&h=500&fit=crop',
      age: '2 years',
      gender: 'Male' as const,
      traits: ['Large', 'Friendly', 'Fluffy'],
      isNew: true,
    },
    {
      id: 'cat-3',
      name: 'Mittens',
      breed: 'Siamese',
      category: 'Cats',
      price: 499.99,
      image: 'https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=500&h=500&fit=crop',
      age: '6 months',
      gender: 'Female' as const,
      traits: ['Playful', 'Vocal', 'Affectionate'],
    },
    {
      id: 'cat-4',
      name: 'Felix',
      breed: 'British Shorthair',
      category: 'Cats',
      price: 699.99,
      image: 'https://images.unsplash.com/photo-1606214174585-fe31582dc6ee?w=500&h=500&fit=crop',
      age: '1 year',
      gender: 'Male' as const,
      traits: ['Calm', 'Independent', 'Beautiful'],
    },
  ],
  birds: [
    {
      id: 'bird-1',
      name: 'Rio',
      breed: 'Amazon Parrot',
      category: 'Birds',
      price: 1499.99,
      image: 'https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=500&h=500&fit=crop',
      age: '2 years',
      gender: 'Male' as const,
      traits: ['Talkative', 'Colorful', 'Intelligent'],
      isFeatured: true,
    },
    {
      id: 'bird-2',
      name: 'Sunny',
      breed: 'Cockatiel',
      category: 'Birds',
      price: 299.99,
      image: 'https://images.unsplash.com/photo-1586098710029-e07b8e9f5b1f?w=500&h=500&fit=crop',
      age: '1 year',
      gender: 'Female' as const,
      traits: ['Friendly', 'Whistles', 'Easy care'],
    },
  ],
  fish: [
    {
      id: 'fish-1',
      name: 'Nemo',
      breed: 'Clownfish',
      category: 'Fish',
      price: 39.99,
      image: 'https://images.unsplash.com/photo-1520301255226-bf5f144451c1?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Colorful', 'Hardy', 'Reef-safe'],
      isNew: true,
    },
    {
      id: 'fish-2',
      name: 'Bubbles',
      breed: 'Betta',
      category: 'Fish',
      price: 24.99,
      image: 'https://images.unsplash.com/photo-1522069169874-c58ec4b76be5?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Beautiful fins', 'Low maintenance', 'Solitary'],
    },
  ],
}

interface Pet {
  id: string;
  name: string;
  breed: string;
  category: string;
  price: number;
  image: string;
  age: string;
  gender: 'Male' | 'Female';
  traits: string[];
  isNew?: boolean;
  isFeatured?: boolean;
  description?: string;
}

interface BackendProduct {
  productId: string;
  name: string;
  categoryId: string;
  description?: string;
}

interface BackendCategory {
  categoryId: string;
  name: string;
  description?: string;
}

export default function FeaturedPets() {
  const [activeTab, setActiveTab] = useState('all')
  const [loading, setLoading] = useState(false)
  const [categories, setCategories] = useState<BackendCategory[]>([])
  const [products, setProducts] = useState<Record<string, BackendProduct[]>>({})

  useEffect(() => {
    fetchCategories()
  }, [])

  const fetchCategories = async () => {
    try {
      setLoading(true)
      const data = await catalogAPI.getCategories()
      setCategories(data)
      // Fetch products for each category
      for (const category of data) {
        const categoryProducts = await catalogAPI.getProductsByCategory(category.categoryId)
        setProducts((prev) => ({
          ...prev,
          [category.categoryId.toLowerCase()]: categoryProducts
        }))
      }
    } catch (error) {
      console.error('Failed to fetch categories:', error)
      // Fall back to mock data
    } finally {
      setLoading(false)
    }
  }

  const getAllPets = (): Pet[] => {
    // Try to use real data first, fall back to mock data
    if (Object.keys(products).length > 0) {
      const allProducts: BackendProduct[] = []
      Object.values(products).forEach((categoryProducts) => {
        allProducts.push(...categoryProducts)
      })
      // Map backend data to frontend format
      return allProducts.map((product: BackendProduct) => ({
        id: product.productId,
        name: product.name,
        breed: product.name,
        category: product.categoryId,
        price: 299.99, // Default price since backend might not have it
        image: `https://images.unsplash.com/photo-${Math.random() > 0.5 ? '1552053831-71594a27632d' : '1587300003388-59208cc962cb'}?w=500&h=500&fit=crop`,
        age: '2 years',
        gender: (Math.random() > 0.5 ? 'Male' : 'Female') as 'Male' | 'Female',
        traits: ['Friendly', 'Vaccinated'],
        description: product.description
      }))
    }
    return [...mockPets.dogs, ...mockPets.cats, ...mockPets.birds, ...mockPets.fish]
  }

  const getFeaturedPets = () => {
    return getAllPets().filter(pet => 'isFeatured' in pet && pet.isFeatured)
  }

  const getNewPets = () => {
    return getAllPets().filter(pet => 'isNew' in pet && pet.isNew)
  }

  const getPetsByCategory = (categoryName: string): Pet[] => {
    const categoryId = categoryName.toUpperCase()
    if (products[categoryId]) {
      return products[categoryId].map((product) => ({
        id: product.productId,
        name: product.name,
        breed: product.name,
        category: categoryName,
        price: 299.99,
        image: `https://images.unsplash.com/photo-${Math.random() > 0.5 ? '1552053831-71594a27632d' : '1587300003388-59208cc962cb'}?w=500&h=500&fit=crop`,
        age: '2 years',
        gender: (Math.random() > 0.5 ? 'Male' : 'Female') as 'Male' | 'Female',
        traits: ['Friendly', 'Vaccinated'],
        description: product.description
      }))
    }
    // Fall back to mock data
    return mockPets[categoryName as keyof typeof mockPets] || []
  }

  return (
    <section className="py-16 bg-muted/30">
      <div className="container mx-auto px-4">
        {/* Section Header */}
        <div className="text-center mb-12">
          <div className="inline-flex items-center gap-2 mb-4">
            <Sparkles className="h-5 w-5 text-primary" />
            <span className="text-sm font-semibold text-primary uppercase tracking-wider">
              Featured Collection
            </span>
            <Sparkles className="h-5 w-5 text-primary" />
          </div>
          <h2 className="text-4xl font-bold mb-4">Meet Our Amazing Pets</h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Each of our pets is special and looking for their forever home. 
            Browse our collection and find your perfect companion.
          </p>
        </div>

        {/* Category Tabs */}
        <Tabs defaultValue="all" className="w-full" onValueChange={setActiveTab}>
          <TabsList className="grid w-full max-w-md mx-auto grid-cols-5 mb-8">
            <TabsTrigger value="all">All Pets</TabsTrigger>
            <TabsTrigger value="dogs">🐕 Dogs</TabsTrigger>
            <TabsTrigger value="cats">🐈 Cats</TabsTrigger>
            <TabsTrigger value="birds">🦜 Birds</TabsTrigger>
            <TabsTrigger value="fish">🐠 Fish</TabsTrigger>
          </TabsList>

          <TabsContent value="all" className="mt-8">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              {getAllPets().slice(0, 8).map((pet) => (
                <PetCard key={pet.id} {...pet} />
              ))}
            </div>
          </TabsContent>

          <TabsContent value="dogs" className="mt-8">
            {loading ? (
              <div className="flex justify-center py-12">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {(getPetsByCategory('dogs').length > 0 ? getPetsByCategory('dogs') : mockPets.dogs).map((pet: Pet) => (
                  <PetCard key={pet.id} {...pet} />
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="cats" className="mt-8">
            {loading ? (
              <div className="flex justify-center py-12">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {(getPetsByCategory('cats').length > 0 ? getPetsByCategory('cats') : mockPets.cats).map((pet: Pet) => (
                  <PetCard key={pet.id} {...pet} />
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="birds" className="mt-8">
            {loading ? (
              <div className="flex justify-center py-12">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {(getPetsByCategory('birds').length > 0 ? getPetsByCategory('birds') : mockPets.birds).map((pet: Pet) => (
                  <PetCard key={pet.id} {...pet} />
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="fish" className="mt-8">
            {loading ? (
              <div className="flex justify-center py-12">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {(getPetsByCategory('fish').length > 0 ? getPetsByCategory('fish') : mockPets.fish).map((pet: Pet) => (
                  <PetCard key={pet.id} {...pet} />
                ))}
              </div>
            )}
          </TabsContent>
        </Tabs>

        {/* View All Button */}
        <div className="text-center mt-12">
          <Link href="/pets">
            <Button size="lg" className="group">
              View All Pets
              <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
            </Button>
          </Link>
        </div>
      </div>
    </section>
  )
}
