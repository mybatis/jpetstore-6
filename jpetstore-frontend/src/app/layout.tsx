import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Header from "@/components/layout/Header";
import { ChatBot } from "@/components/chat/ChatBot";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "PetStore - Your Trusted Pet Companion",
  description: "Find your perfect pet companion at PetStore. Dogs, cats, birds, fish, and reptiles available with health guarantees and expert support.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.className} antialiased`}>
        <Header />
        {children}
        <ChatBot />
        <footer className="bg-muted border-t mt-auto">
          <div className="container mx-auto px-4 py-8">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
              <div>
                <h3 className="font-semibold mb-3">About PetStore</h3>
                <ul className="space-y-2 text-sm text-muted-foreground">
                  <li><a href="/about" className="hover:text-foreground">Our Story</a></li>
                  <li><a href="/careers" className="hover:text-foreground">Careers</a></li>
                  <li><a href="/press" className="hover:text-foreground">Press</a></li>
                </ul>
              </div>
              <div>
                <h3 className="font-semibold mb-3">Customer Service</h3>
                <ul className="space-y-2 text-sm text-muted-foreground">
                  <li><a href="/contact" className="hover:text-foreground">Contact Us</a></li>
                  <li><a href="/shipping" className="hover:text-foreground">Shipping Info</a></li>
                  <li><a href="/returns" className="hover:text-foreground">Returns</a></li>
                </ul>
              </div>
              <div>
                <h3 className="font-semibold mb-3">Pet Care</h3>
                <ul className="space-y-2 text-sm text-muted-foreground">
                  <li><a href="/care-guides" className="hover:text-foreground">Care Guides</a></li>
                  <li><a href="/adoption" className="hover:text-foreground">Adoption Process</a></li>
                  <li><a href="/health" className="hover:text-foreground">Pet Health</a></li>
                </ul>
              </div>
              <div>
                <h3 className="font-semibold mb-3">Connect</h3>
                <ul className="space-y-2 text-sm text-muted-foreground">
                  <li><a href="#" className="hover:text-foreground">Facebook</a></li>
                  <li><a href="#" className="hover:text-foreground">Instagram</a></li>
                  <li><a href="#" className="hover:text-foreground">Twitter</a></li>
                </ul>
              </div>
            </div>
            <div className="border-t mt-8 pt-8 text-center text-sm text-muted-foreground">
              <p>&copy; 2024 PetStore. All rights reserved. | Modern JPetStore powered by Next.js & Spring Boot</p>
            </div>
          </div>
        </footer>
      </body>
    </html>
  );
}
