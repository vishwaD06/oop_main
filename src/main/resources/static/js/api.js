// ── ShareSphere API Client with Hybrid Backend + Instant GitHub Pages Demo Mock ──
const isGitHubPages = typeof window !== 'undefined' && (window.location.hostname.includes('github.io') || window.location.protocol === 'file:');

const MOCK_CATEGORIES = [
  { id: 1, name: "Electronics", icon: "💻", description: "Phones, laptops, cameras, gadgets" },
  { id: 2, name: "Books", icon: "📚", description: "Textbooks, novels, reference books" },
  { id: 3, name: "Calculators", icon: "🧮", description: "Scientific and graphing calculators" },
  { id: 4, name: "Engineering Tools", icon: "📐", description: "Drawing sets, instruments, tools" },
  { id: 5, name: "Lab Equipment", icon: "🔬", description: "Microscopes, beakers, lab instruments" },
  { id: 6, name: "Sports Equipment", icon: "⚽", description: "Balls, rackets, gym equipment" },
  { id: 7, name: "Furniture", icon: "🪑", description: "Tables, chairs, shelves, lamps" },
  { id: 8, name: "Cycles", icon: "🚲", description: "Bicycles and accessories" },
  { id: 9, name: "Bags", icon: "🎒", description: "Backpacks, laptop bags, luggage" },
  { id: 10, name: "Musical Instruments", icon: "🎸", description: "Guitars, keyboards, drums" },
  { id: 11, name: "Event Equipment", icon: "🎤", description: "Projectors, mics, displays" },
  { id: 12, name: "Hostel Items", icon: "🏠", description: "Kettles, buckets, fans, utensils" },
  { id: 13, name: "Study Materials", icon: "📝", description: "Notes, question papers, assignments" },
  { id: 14, name: "Other", icon: "📦", description: "Everything else" }
];

const MOCK_ITEMS = [
  {
    id: 1,
    name: "Casio FX-991CW Scientific Calculator",
    description: "Latest Casio FX-991CW, 552 functions, natural display. Perfect for semester exams.",
    category: { id: 3, name: "Calculators", icon: "🧮" },
    condition: "Like New",
    price: 900,
    rentalPricePerDay: 30,
    securityDeposit: 300,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Hostel A",
    averageRating: 4.8,
    owner: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 },
    imageUrls: ["https://images.unsplash.com/photo-1594980596870-8aa52a78d8cd?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 2,
    name: "DSLR Camera Canon 200D",
    description: "Canon 200D with 18-55mm kit lens, 24.1MP. Great for photography projects and events.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "Good",
    price: 28000,
    rentalPricePerDay: 300,
    securityDeposit: 2000,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Hostel A",
    averageRating: 5.0,
    owner: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 },
    imageUrls: ["https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 3,
    name: "Arduino Uno Starter Kit",
    description: "Arduino Uno R3 with 37-sensor kit, breadboard, jumper wires. Ideal for IoT projects.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "New",
    price: 1500,
    rentalPricePerDay: 50,
    securityDeposit: 500,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Lab Block",
    averageRating: 4.7,
    owner: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
    imageUrls: ["https://images.unsplash.com/photo-1553406830-ef2513450d76?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 4,
    name: "Engineering Drawing Kit",
    description: "Staedtler 551 drawing set with compass, mini-drafter, set squares. First year essential.",
    category: { id: 4, name: "Engineering Tools", icon: "📐" },
    condition: "Good",
    price: 800,
    rentalPricePerDay: 20,
    securityDeposit: 200,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Block C",
    averageRating: 4.6,
    owner: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    imageUrls: ["https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 5,
    name: "Mountain Bicycle - Hero Sprint",
    description: "Hero Sprint 21-speed MTB, well maintained. Perfect for campus commute.",
    category: { id: 8, name: "Cycles", icon: "🚲" },
    condition: "Good",
    price: 6000,
    rentalPricePerDay: 100,
    securityDeposit: 1000,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Parking Lot B",
    averageRating: 4.9,
    owner: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    imageUrls: ["https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 6,
    name: "Data Structures & Algorithms - CLRS 4th Ed",
    description: "Introduction to Algorithms by CLRS. Excellent condition, no markings.",
    category: { id: 2, name: "Books", icon: "📚" },
    condition: "Like New",
    price: 1200,
    rentalPricePerDay: null,
    securityDeposit: null,
    listingType: "SELL",
    status: "AVAILABLE",
    location: "Library Block",
    averageRating: 5.0,
    owner: { id: 5, name: "Sneha Patel", email: "sneha@college.edu", college: "IIT Campus", location: "Library Block", averageRating: 5.0 },
    imageUrls: ["https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 7,
    name: "Raspberry Pi 4 Model B (4GB)",
    description: "Raspberry Pi 4B 4GB RAM, unused. Comes with 32GB SD card and cooling case.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "New",
    price: 5500,
    rentalPricePerDay: 150,
    securityDeposit: 1000,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Lab Block",
    averageRating: 4.9,
    owner: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
    imageUrls: ["https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?w=600&auto=format&fit=crop&q=80"]
  },
  {
    id: 8,
    name: "Wireless Bluetooth Speaker - JBL Go 3",
    description: "JBL Go 3, waterproof, 5hr battery. Great for room study jams or outdoor trips.",
    category: { id: 1, name: "Electronics", icon: "💻" },
    condition: "Like New",
    price: 2500,
    rentalPricePerDay: 70,
    securityDeposit: 500,
    listingType: "SELL_AND_RENT",
    status: "AVAILABLE",
    location: "Hostel B",
    averageRating: 4.8,
    owner: { id: 5, name: "Sneha Patel", email: "sneha@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 5.0 },
    imageUrls: ["https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&auto=format&fit=crop&q=80"]
  }
];

const MOCK_LANDMARKS = [
  "Central Library Foyer (Ground Floor)",
  "Student Activity Center (SAC) Plaza",
  "Main Food Court / Canteen",
  "Hostel 1 / Ramanujan Hall Gate",
  "Hostel 2 / Aryabhata Hall Gate",
  "Hostel 3 / Kalam Hall Gate",
  "Hostel 4 / Sarabhai Hall Gate",
  "Hostel A - Girls Wing Entrance",
  "Hostel B - Boys Wing Entrance",
  "Hostel C - International Hostel Lobby",
  "Academic Complex Block A",
  "Academic Complex Block B",
  "Sports Complex & Badminton Court",
  "Campus Main Gate (Gate 1)"
];

const MOCK_REQUESTS = [
  {
    id: 1,
    title: "Need HDMI adapter for 2 hours today",
    description: "Need a HDMI to VGA/Type-C adapter for a seminar hall presentation today between 4-6 PM.",
    category: { id: 11, name: "Event Equipment", icon: "🎤" },
    requester: { id: 5, name: "Sneha Patel", email: "sneha@college.edu", college: "IIT Campus", location: "Library Block", averageRating: 5.0 },
    location: "Seminar Hall",
    landmark: "Student Activity Center (SAC) Plaza",
    budget: 100,
    neededTimeframe: "Today 4-6 PM",
    urgency: "URGENT",
    status: "OPEN",
    offerCount: 1,
    offers: [],
    createdAt: "2026-11-17T10:30:00"
  },
  {
    id: 2,
    title: "Looking for badminton racket for weekend",
    description: "Need a racket for hostel court this Saturday morning. Can return Sunday evening.",
    category: { id: 6, name: "Sports Equipment", icon: "⚽" },
    requester: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    location: "Sports Complex",
    landmark: "Sports Complex & Badminton Court",
    budget: 150,
    neededTimeframe: "Saturday 8-11 AM",
    urgency: "MEDIUM",
    status: "OPEN",
    offerCount: 0,
    offers: [],
    createdAt: "2026-11-16T18:15:00"
  },
  {
    id: 3,
    title: "Need scientific calculator for exam week",
    description: "Need a Casio FX-991 or equivalent for 3 days during exam week. Prefer hostel handover.",
    category: { id: 3, name: "Calculators", icon: "🧮" },
    requester: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 },
    location: "Hostel A",
    landmark: "Hostel A - Girls Wing Entrance",
    budget: 120,
    neededTimeframe: "3 days next week",
    urgency: "MEDIUM",
    status: "FULFILLED",
    offerCount: 2,
    offers: [],
    createdAt: "2026-11-15T09:00:00"
  }
];
MOCK_REQUESTS[0].offers = [{
  id: 11,
  requestId: 1,
  offerer: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
  message: "I have an HDMI to Type-C adapter and can meet at SAC plaza.",
  priceOffered: 50,
  linkedItem: null,
  createdAt: "2026-11-17T11:00:00"
}];
MOCK_REQUESTS[2].offers = [
  {
    id: 21,
    requestId: 3,
    offerer: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
    message: "I can lend my Casio FX-991 for the week.",
    priceOffered: 100,
    linkedItem: MOCK_ITEMS[0],
    createdAt: "2026-11-15T10:00:00"
  },
  {
    id: 22,
    requestId: 3,
    offerer: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    message: "I have a scientific calculator available.",
    priceOffered: 120,
    linkedItem: null,
    createdAt: "2026-11-15T12:00:00"
  }
];
const MOCK_MY_REQUESTS = [MOCK_REQUESTS[2]];

const MOCK_RIDES = [
  {
    id: 1,
    organizer: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 },
    origin: "Campus Main Gate (Gate 1)",
    destination: "Hyderabad Railway Station",
    departureTime: "2026-11-20T18:30:00",
    totalSeats: 4,
    availableSeats: 2,
    pricePerSeat: 150,
    vehicleType: "Cab (Uber/Ola)",
    notes: "Leave at sharp time. Luggage allowed up to 2 bags.",
    status: "OPEN",
    bookings: [
      { id: 31, rideId: 1, passenger: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 }, seatsBooked: 1, bookedAt: "2026-11-17T12:00:00" },
      { id: 32, rideId: 1, passenger: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 }, seatsBooked: 1, bookedAt: "2026-11-17T13:00:00" }
    ],
    createdAt: "2026-11-17T09:00:00",
    userJoined: false
  },
  {
    id: 2,
    organizer: { id: 3, name: "Priya Verma", email: "priya@college.edu", college: "IIT Campus", location: "Hostel B", averageRating: 4.8 },
    origin: "Hostel B - Boys Wing Entrance",
    destination: "RGIA Airport Terminal 1",
    departureTime: "2026-11-21T05:00:00",
    totalSeats: 3,
    availableSeats: 0,
    pricePerSeat: 450,
    vehicleType: "Personal Car",
    notes: "Early morning airport drop. Meet at hostel gate 10 mins early.",
    status: "FULL",
    bookings: [],
    createdAt: "2026-11-16T20:00:00",
    userJoined: false
  },
  {
    id: 3,
    organizer: { id: 4, name: "Arjun Mehta", email: "arjun@college.edu", college: "IIT Campus", location: "Block C", averageRating: 4.7 },
    origin: "Campus Main Gate (Gate 1)",
    destination: "Secunderabad Metro Station",
    departureTime: "2026-11-22T09:15:00",
    totalSeats: 3,
    availableSeats: 2,
    pricePerSeat: 80,
    vehicleType: "Auto Rickshaw",
    notes: "Shared auto to metro. Ideal for weekend plans.",
    status: "OPEN",
    bookings: [
      { id: 33, rideId: 3, passenger: { id: 2, name: "Rahul Sharma", email: "rahul@college.edu", college: "IIT Campus", location: "Hostel A", averageRating: 4.9 }, seatsBooked: 1, bookedAt: "2026-11-17T14:00:00" }
    ],
    createdAt: "2026-11-17T08:30:00",
    userJoined: true
  }
];
const MOCK_MY_RIDES = [MOCK_RIDES[0], MOCK_RIDES[2]];

const API = {
  baseUrl: '',
  getToken: () => localStorage.getItem('ss_token'),

  async request(method, path, body=null, isForm=false) {
    if (isGitHubPages) {
      return this.handleMockFallback(method, path, body);
    }

    const headers = {};
    const token = this.getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;
    if (!isForm) headers['Content-Type'] = 'application/json';
    const opts = { method, headers };
    if (body) opts.body = isForm ? body : JSON.stringify(body);

    try {
      const res = await fetch(this.baseUrl + path, opts);
      if (res.status === 204) return null;
      if (res.ok) {
        return await res.json().catch(()=>({}));
      }
      const data = await res.json().catch(()=>({}));
      throw new Error(data.message || data.error || 'Request failed');
    } catch (err) {
      return this.handleMockFallback(method, path, body);
    }
  },

  handleMockFallback(method, path, body) {
    if (path === '/api/categories') return MOCK_CATEGORIES;
    if (path === '/api/landmarks') return MOCK_LANDMARKS;
    if (path === '/api/wallet') return { userId: 2, userName: 'Rahul Sharma', walletBalance: 5200, escrowBalance: 300, totalBalance: 5500 };
    if (path === '/api/users/me') {
      const stored = JSON.parse(localStorage.getItem('ss_user') || 'null');
      return stored || { id: 2, userId: 2, name: 'Rahul Sharma', email: 'rahul@college.edu', role: 'STUDENT', averageRating: 4.9, totalRatings: 12 };
    }
    if (path === '/api/smart/pricing') {
      return {
        suggestedSellPrice: 850,
        suggestedRentalPerDay: 35,
        suggestedSecurityDeposit: 300,
        estimatedMrp: 1500,
        depreciationRatePercent: 30,
        surgeMultiplier: 1.25,
        demandLevel: 'PEAK_EXAM_SURGE',
        spikeReason: 'Peak exam season demand',
        explanation: 'Estimated from condition and campus demand.'
      };
    }
    if (path === '/api/smart/moderation/check') {
      return { approved: true, flagged: false, categoryDetected: 'NONE', flaggedKeywords: [], message: 'Listing passes all campus safety checks.' };
    }
    if (path === '/api/items/popular' || path === '/api/items/latest') return MOCK_ITEMS;
    if (path === '/api/items/my') return [];
    if (path.includes('/images')) return { imageUrl: 'images/placeholder.svg' };
    if (path.startsWith('/api/items/search')) {
      const q = new URLSearchParams(path.split('?')[1]||'');
      const catId = q.get('categoryId');
      const kw = (q.get('keyword')||'').toLowerCase();
      let filtered = [...MOCK_ITEMS];
      if (catId) filtered = filtered.filter(i => i.category?.id == catId);
      if (kw) filtered = filtered.filter(i => i.name.toLowerCase().includes(kw) || i.description.toLowerCase().includes(kw));
      return {
        content: filtered,
        totalPages: 1,
        totalElements: filtered.length,
        size: 12,
        number: 0
      };
    }
    if (path.startsWith('/api/items/')) {
      const id = parseInt(path.split('/')[3]);
      const found = MOCK_ITEMS.find(i => i.id === id) || MOCK_ITEMS[0];
      return found;
    }
    if (path === '/api/requests' && method === 'GET') return MOCK_REQUESTS.filter(r => r.status === 'OPEN');
    if (path === '/api/requests/my') return MOCK_MY_REQUESTS;
    if (path === '/api/requests' && method === 'POST') {
      return { id: 99, status: 'OPEN', offerCount: 0, offers: [], createdAt: new Date().toISOString(), ...body };
    }
    if (path.startsWith('/api/requests/')) {
      const parts = path.split('/');
      const id = parseInt(parts[3]);
      const req = MOCK_REQUESTS.find(r => r.id === id) || MOCK_REQUESTS[0];
      if (parts[4] === 'offers' && method === 'POST') {
        return { id: 99, requestId: id, offerer: { id: 2, name: 'Rahul Sharma' }, createdAt: new Date().toISOString(), ...body };
      }
      if (parts[4] === 'fulfill' && method === 'POST') {
        req.status = 'FULFILLED';
        return req;
      }
      return req;
    }
    if (path === '/api/rides' && method === 'GET') return MOCK_RIDES;
    if (path === '/api/rides/my') return MOCK_MY_RIDES;
    if (path === '/api/rides' && method === 'POST') {
      return { id: 99, status: 'OPEN', availableSeats: body?.totalSeats || 1, bookings: [], createdAt: new Date().toISOString(), organizer: { id: 2, name: 'Rahul Sharma' }, userJoined: false, ...body };
    }
    if (path.startsWith('/api/rides/')) {
      const parts = path.split('/');
      const id = parseInt(parts[3]);
      const ride = MOCK_RIDES.find(r => r.id === id) || MOCK_RIDES[0];
      if (parts[4] === 'join' && method === 'POST') {
        ride.availableSeats = Math.max(0, ride.availableSeats - 1);
        ride.bookings = ride.bookings || [];
        ride.bookings.push({ id: 99, rideId: id, passenger: { id: 2, name: 'Rahul Sharma' }, seatsBooked: 1, bookedAt: new Date().toISOString() });
        ride.userJoined = true;
        if (ride.availableSeats === 0) ride.status = 'FULL';
        return ride;
      }
      if (parts[4] === 'cancel' && method === 'POST') {
        ride.availableSeats += 1;
        ride.bookings = (ride.bookings || []).filter(b => b.passenger?.id !== 2);
        ride.userJoined = false;
        if (ride.status === 'FULL') ride.status = 'OPEN';
        return ride;
      }
      return ride;
    }
    if (path.startsWith('/api/ai/search')) {
      const q = new URLSearchParams(path.split('?')[1]||'').get('query') || '';
      return {
        aiSuggestion: `AI matched ${MOCK_ITEMS.length} items based on "${q}" across categories.`,
        results: MOCK_ITEMS
      };
    }
    if (path.startsWith('/api/ai/similar')) return MOCK_ITEMS.slice(0, 3);
    if (path === '/api/admin/stats') {
      return {
        totalUsers: 6,
        totalItems: 15,
        availableItems: 14,
        activeRentals: 3,
        completedRentals: 8,
        totalOrders: 5,
        paidOrders: 4,
        pendingReports: 0,
        blockedUsers: 0
      };
    }
    if (path === '/api/auth/login') {
      const email = body?.email || 'rahul@college.edu';
      const isAdmin = email.includes('admin');
      const mockUser = {
        token: 'demo-token-12345',
        userId: isAdmin ? 1 : 2,
        name: isAdmin ? 'Admin' : 'Rahul Sharma',
        email: email,
        role: isAdmin ? 'ADMIN' : 'STUDENT'
      };
      return mockUser;
    }
    if (path === '/api/auth/register') {
      return {
        token: 'demo-token-12345',
        userId: 99,
        name: body?.name || 'New Student',
        email: body?.email || 'student@college.edu',
        role: 'STUDENT'
      };
    }
    if (path === '/api/items/my' || path === '/api/favorites' || path === '/api/rentals/my' || path === '/api/rentals/incoming' || path === '/api/orders/my' || path === '/api/conversations' || path === '/api/notifications') {
      return [];
    }
    if (path === '/api/notifications/unread-count') return { count: 0 };
    return {};
  },

  get: (path) => API.request('GET', path),
  post: (path, body) => API.request('POST', path, body),
  put: (path, body) => API.request('PUT', path, body),
  patch: (path, body) => API.request('PATCH', path, body),
  delete: (path) => API.request('DELETE', path),
  uploadFile: (path, formData) => API.request('POST', path, formData, true),
};
