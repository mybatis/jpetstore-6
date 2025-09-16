import HeroSection from '@/components/home/HeroSection'
import FeaturedPets from '@/components/home/FeaturedPets'

export default function Home() {
  return (
    <main className="min-h-screen">
      <HeroSection />
      <FeaturedPets />
      
      {/* Additional sections can be added here */}
      <section className="py-16 bg-background">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold mb-4">Why Choose Our Pet Store?</h2>
            <p className="text-lg text-muted-foreground mb-8">
              We&apos;re committed to connecting loving families with their perfect pet companions. 
              With over 20 years of experience, we ensure every pet finds their forever home.
            </p>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-12">
              <div className="text-center">
                <div className="text-4xl mb-4">🏆</div>
                <h3 className="text-xl font-semibold mb-2">Trusted Since 2004</h3>
                <p className="text-muted-foreground">
                  Thousands of happy families and healthy pets
                </p>
              </div>
              <div className="text-center">
                <div className="text-4xl mb-4">💝</div>
                <h3 className="text-xl font-semibold mb-2">Health Guaranteed</h3>
                <p className="text-muted-foreground">
                  All pets come with complete health certifications
                </p>
              </div>
              <div className="text-center">
                <div className="text-4xl mb-4">🌟</div>
                <h3 className="text-xl font-semibold mb-2">Expert Support</h3>
                <p className="text-muted-foreground">
                  Lifetime guidance from our pet care specialists
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Newsletter Section */}
      <section className="py-16 bg-primary text-primary-foreground">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-3xl font-bold mb-4">Stay Connected</h2>
          <p className="text-lg mb-8 opacity-90">
            Get updates on new arrivals and exclusive offers
          </p>
          <div className="max-w-md mx-auto flex gap-4">
            <input
              type="email"
              placeholder="Enter your email"
              className="flex-1 px-4 py-2 rounded-lg bg-background/10 backdrop-blur placeholder:text-primary-foreground/60 text-primary-foreground border border-primary-foreground/20"
            />
            <button className="px-6 py-2 bg-background text-primary rounded-lg font-semibold hover:bg-background/90 transition-colors">
              Subscribe
            </button>
          </div>
        </div>
      </section>
    </main>
  )
}
