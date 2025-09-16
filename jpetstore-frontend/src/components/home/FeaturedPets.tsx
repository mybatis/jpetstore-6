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
      id: 'K9-BD-01',
      name: 'Bulldog',
      breed: 'Bulldog',
      category: 'Dogs',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Friendly', 'From England', 'Loyal'],
      description: 'Friendly dog from England',
      isFeatured: true,
    },
    {
      id: 'K9-PO-02',
      name: 'Poodle',
      breed: 'Poodle',
      category: 'Dogs',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1616190264687-b7ebf7aa2eb4?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Female' as const,
      traits: ['Cute', 'From France', 'Intelligent'],
      description: 'Cute dog from France',
    },
    {
      id: 'K9-DL-01',
      name: 'Dalmation',
      breed: 'Dalmation',
      category: 'Dogs',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1605568427561-40dd23c2acea?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Spotted', 'Fire Station', 'Active'],
      description: 'Great dog for a Fire Station',
      isFeatured: true,
    },
    {
      id: 'K9-RT-01',
      name: 'Golden Retriever',
      breed: 'Golden Retriever',
      category: 'Dogs',
      price: 155.29,
      image: 'https://images.unsplash.com/photo-1552053831-71594a27632d?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Female' as const,
      traits: ['Family-friendly', 'Loyal', 'Gentle'],
      description: 'Great family dog',
      isNew: true,
    },
    {
      id: 'K9-RT-02',
      name: 'Labrador Retriever',
      breed: 'Labrador Retriever',
      category: 'Dogs',
      price: 135.50,
      image: 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Hunting', 'Active', 'Loyal'],
      description: 'Great hunting dog',
    },
    {
      id: 'K9-CW-01',
      name: 'Chihuahua',
      breed: 'Chihuahua',
      category: 'Dogs',
      price: 125.50,
      image: 'https://images.unsplash.com/photo-1541364983171-a8ba01e95cfc?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Companion', 'Small', 'Alert'],
      description: 'Great companion dog',
      isNew: true,
    },
  ],
  cats: [
    {
      id: 'FL-DSH-01',
      name: 'Manx',
      breed: 'Manx',
      category: 'Cats',
      price: 58.50,
      image: 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Female' as const,
      traits: ['Tailless', 'Mouse hunter', 'Independent'],
      description: 'Great for reducing mouse populations',
      isFeatured: true,
    },
    {
      id: 'FL-DLH-02',
      name: 'Persian',
      breed: 'Persian',
      category: 'Cats',
      price: 93.50,
      image: 'https://images.unsplash.com/photo-1513360371669-4adf3dd7dff8?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Long hair', 'Princess-like', 'Friendly'],
      description: 'Friendly house cat, doubles as a princess',
      isNew: true,
    },
  ],
  birds: [
    {
      id: 'AV-CB-01',
      name: 'Amazon Parrot',
      breed: 'Amazon Parrot',
      category: 'Birds',
      price: 193.50,
      image: 'https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Talkative', 'Long-lived', 'Intelligent'],
      description: 'Great companion for up to 75 years',
      isFeatured: true,
    },
    {
      id: 'AV-SB-02',
      name: 'Finch',
      breed: 'Finch',
      category: 'Birds',
      price: 15.50,
      image: 'https://images.unsplash.com/photo-1586098710029-e07b8e9f5b1f?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Small', 'Peaceful', 'Easy care'],
      description: 'Great stress reliever',
    },
  ],
  fish: [
    {
      id: 'FI-SW-01',
      name: 'Angelfish',
      breed: 'Angelfish',
      category: 'Fish',
      price: 16.50,
      image: 'https://images.unsplash.com/photo-1520301255226-bf5f144451c1?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Salt water', 'From Australia', 'Beautiful'],
      description: 'Salt Water fish from Australia',
      isNew: true,
    },
    {
      id: 'FI-SW-02',
      name: 'Tiger Shark',
      breed: 'Tiger Shark',
      category: 'Fish',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Salt water', 'From Australia', 'Predator'],
      description: 'Salt Water fish from Australia',
      isFeatured: true,
    },
    {
      id: 'FI-FW-01',
      name: 'Koi',
      breed: 'Koi',
      category: 'Fish',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Fresh water', 'From Japan', 'Colorful'],
      description: 'Fresh Water fish from Japan',
    },
    {
      id: 'FI-FW-02',
      name: 'Goldfish',
      breed: 'Goldfish',
      category: 'Fish',
      price: 5.50,
      image: 'https://images.unsplash.com/photo-1522069169874-c58ec4b76be5?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Fresh water', 'From China', 'Easy care'],
      description: 'Fresh Water fish from China',
    },
  ],
  reptiles: [
    {
      id: 'RP-SN-01',
      name: 'Rattlesnake',
      breed: 'Rattlesnake',
      category: 'Reptiles',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1516205651411-aef33a44f7c2?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Venomous', 'Watch dog', 'Dangerous'],
      description: 'Doubles as a watch dog',
      isFeatured: true,
    },
    {
      id: 'RP-LI-02',
      name: 'Iguana',
      breed: 'Iguana',
      category: 'Reptiles',
      price: 18.50,
      image: 'https://images.unsplash.com/photo-1520315342629-6ea920342047?w=500&h=500&fit=crop',
      age: 'Adult',
      gender: 'Male' as const,
      traits: ['Green', 'Friendly', 'Herbivore'],
      description: 'Friendly green friend',
      isNew: true,
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
    return [...mockPets.dogs, ...mockPets.cats, ...mockPets.birds, ...mockPets.fish, ...mockPets.reptiles]
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
          <TabsList className="grid w-full max-w-2xl mx-auto grid-cols-6 mb-8">
            <TabsTrigger value="all">All Pets</TabsTrigger>
            <TabsTrigger value="dogs">🐕 Dogs</TabsTrigger>
            <TabsTrigger value="cats">🐈 Cats</TabsTrigger>
            <TabsTrigger value="birds">🦜 Birds</TabsTrigger>
            <TabsTrigger value="fish">🐠 Fish</TabsTrigger>
            <TabsTrigger value="reptiles">🦎 Reptiles</TabsTrigger>
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

          <TabsContent value="reptiles" className="mt-8">
            {loading ? (
              <div className="flex justify-center py-12">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {(getPetsByCategory('reptiles').length > 0 ? getPetsByCategory('reptiles') : mockPets.reptiles).map((pet: Pet) => (
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
