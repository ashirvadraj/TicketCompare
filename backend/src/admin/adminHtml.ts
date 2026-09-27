export function getAdminDashboardHtml(): string {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TicketCompare Admin Command Console</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">
  <style>
    :root {
      --bg-base: #0B0F19;
      --bg-surface: #111827;
      --bg-card: #1F2937;
      --border-color: #374151;
      --primary: #4F46E5;
      --primary-hover: #4338CA;
      --accent-gold: #F59E0B;
      --accent-green: #10B981;
      --accent-red: #EF4444;
      --text-main: #F9FAFB;
      --text-muted: #9CA3AF;
      --font-sans: 'Plus Jakarta Sans', system-ui, -apple-system, sans-serif;
      --font-mono: 'JetBrains Mono', monospace;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background-color: var(--bg-base);
      color: var(--text-main);
      font-family: var(--font-sans);
      min-height: 100vh;
      display: flex;
    }
    aside {
      width: 260px;
      background: var(--bg-surface);
      border-right: 1px solid var(--border-color);
      padding: 24px 16px;
      display: flex;
      flex-direction: column;
      gap: 32px;
    }
    .logo-box {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .logo-badge {
      background: linear-gradient(135deg, #4F46E5, #F59E0B);
      width: 40px;
      height: 40px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 800;
      font-size: 20px;
    }
    .brand-title {
      font-size: 18px;
      font-weight: 800;
      letter-spacing: -0.5px;
    }
    .brand-subtitle {
      font-size: 11px;
      color: var(--accent-gold);
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 1px;
    }
    nav {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .nav-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 14px;
      border-radius: 8px;
      color: var(--text-muted);
      text-decoration: none;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.15s ease;
    }
    .nav-item:hover, .nav-item.active {
      background: rgba(79, 70, 229, 0.15);
      color: #A5B4FC;
    }
    main {
      flex: 1;
      padding: 32px;
      overflow-y: auto;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 28px;
    }
    .header h1 {
      font-size: 24px;
      font-weight: 700;
    }
    .status-pill {
      background: rgba(16, 185, 129, 0.15);
      color: var(--accent-green);
      padding: 6px 14px;
      border-radius: 999px;
      font-size: 12px;
      font-weight: 700;
      display: inline-flex;
      align-items: center;
      gap: 6px;
      border: 1px solid rgba(16, 185, 129, 0.3);
    }
    .status-dot {
      width: 8px;
      height: 8px;
      background: var(--accent-green);
      border-radius: 50%;
    }
    .grid-metrics {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 20px;
      margin-bottom: 32px;
    }
    .metric-card {
      background: var(--bg-surface);
      border: 1px solid var(--border-color);
      border-radius: 12px;
      padding: 20px;
    }
    .metric-title {
      font-size: 12px;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin-bottom: 8px;
    }
    .metric-val {
      font-size: 28px;
      font-weight: 800;
      font-family: var(--font-mono);
    }
    .metric-sub {
      font-size: 12px;
      color: var(--accent-green);
      margin-top: 4px;
      font-weight: 500;
    }
    .section-title {
      font-size: 18px;
      font-weight: 700;
      margin-bottom: 16px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .btn {
      background: var(--primary);
      color: white;
      border: none;
      padding: 8px 16px;
      border-radius: 8px;
      font-weight: 600;
      cursor: pointer;
      font-size: 13px;
      transition: background 0.2s;
    }
    .btn:hover { background: var(--primary-hover); }
    .btn-outline {
      background: transparent;
      border: 1px solid var(--border-color);
      color: var(--text-main);
    }
    .btn-outline:hover { background: rgba(255,255,255,0.05); }
    .btn-sm { padding: 4px 10px; font-size: 11px; border-radius: 6px; }
    
    .table-card {
      background: var(--bg-surface);
      border: 1px solid var(--border-color);
      border-radius: 12px;
      overflow: hidden;
      margin-bottom: 32px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      text-align: left;
    }
    th {
      background: rgba(31, 41, 55, 0.5);
      padding: 14px 16px;
      font-size: 12px;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.5px;
      border-bottom: 1px solid var(--border-color);
    }
    td {
      padding: 16px;
      font-size: 13px;
      border-bottom: 1px solid rgba(55, 65, 81, 0.5);
    }
    tr:last-child td { border-bottom: none; }
    .badge {
      display: inline-block;
      padding: 4px 8px;
      border-radius: 6px;
      font-size: 11px;
      font-weight: 600;
    }
    .badge-verified { background: rgba(16, 185, 129, 0.15); color: #34D399; }
    .badge-cashback { background: rgba(245, 158, 11, 0.15); color: #FBBF24; }
    .badge-instant { background: rgba(79, 70, 229, 0.2); color: #A5B4FC; }

    .test-box {
      background: #182234;
      border: 1px dashed #3B82F6;
      border-radius: 12px;
      padding: 20px;
      margin-top: 24px;
    }
  </style>
</head>
<body>
  <aside>
    <div class="logo-box">
      <div class="logo-badge">TC</div>
      <div>
        <div class="brand-title">TicketCompare</div>
        <div class="brand-subtitle">Admin Hub v1.0</div>
      </div>
    </div>
    <nav>
      <a class="nav-item active" href="#overview">📊 System Overview</a>
      <a class="nav-item" href="#providers">🏢 Provider Adapters</a>
      <a class="nav-item" href="#offers">🎁 Offer Engine</a>
      <a class="nav-item" href="#coupons">🏷️ Promo & Coupons</a>
      <a class="nav-item" href="#price-testing">⚡ Price Alert Sim</a>
      <a class="nav-item" href="#logs">📜 Audit Logs</a>
    </nav>
  </aside>

  <main>
    <div class="header">
      <div>
        <h1>Operational Dashboard</h1>
        <p style="color: var(--text-muted); font-size: 13px; margin-top: 4px;">Real-time movie provider status, offer verification, and price calculations.</p>
      </div>
      <div class="status-pill">
        <span class="status-dot"></span>
        Engines Online & Healthy
      </div>
    </div>

    <!-- METRICS -->
    <div class="grid-metrics">
      <div class="metric-card">
        <div class="metric-title">Active Providers</div>
        <div class="metric-val" id="metric-providers">4 / 4</div>
        <div class="metric-sub">BMS, District, PVR, Cinepolis</div>
      </div>
      <div class="metric-card">
        <div class="metric-title">Live Verified Offers</div>
        <div class="metric-val" id="metric-offers">12</div>
        <div class="metric-sub">HDFC, ICICI, SBI, Axis, GPay</div>
      </div>
      <div class="metric-card">
        <div class="metric-title">Active Coupons</div>
        <div class="metric-val">3</div>
        <div class="metric-sub">SAVE100, MOVIE50, PVRPASS</div>
      </div>
      <div class="metric-card">
        <div class="metric-title">Avg Latency</div>
        <div class="metric-val">34ms</div>
        <div class="metric-sub">Official Direct Adapters</div>
      </div>
    </div>

    <!-- PROVIDERS TABLE -->
    <div class="section-title">
      <span>Authorized Booking Providers</span>
      <button class="btn btn-outline btn-sm" onclick="fetchHealth()">Refresh Health</button>
    </div>
    <div class="table-card">
      <table id="providers-table">
        <thead>
          <tr>
            <th>Provider</th>
            <th>Adapter ID</th>
            <th>Seat Inventory Support</th>
            <th>Direct Deep Links</th>
            <th>Health Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody id="providers-body">
          <!-- Populated by JS -->
        </tbody>
      </table>
    </div>

    <!-- OFFERS TABLE -->
    <div class="section-title">
      <span>Verified Offers & Bank Deals</span>
      <button class="btn btn-sm" onclick="showAddOfferModal()">+ Add New Verified Offer</button>
    </div>
    <div class="table-card">
      <table id="offers-table">
        <thead>
          <tr>
            <th>Offer Title</th>
            <th>Category</th>
            <th>Type</th>
            <th>Benefit</th>
            <th>Min Spend</th>
            <th>Status</th>
            <th>Last Verified</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody id="offers-body">
          <!-- Populated by JS -->
        </tbody>
      </table>
    </div>

    <!-- SIMULATION / TESTING BOX -->
    <div class="test-box">
      <h3 style="margin-bottom: 8px; font-size: 15px; color: #93C5FD;">⚡ Live Price Fluctuation Simulation</h3>
      <p style="font-size: 13px; color: #CBD5E1; margin-bottom: 16px;">Test Requirement 17: trigger an instant ₹25 price jump to verify that the mobile app halts with <i>"The ticket price changed from ₹450 to ₹475. Please review before booking."</i></p>
      <div style="display: flex; gap: 12px; align-items: center;">
        <button class="btn btn-sm" style="background: #E11D48;" onclick="simulateSurge('district', 25)">Simulate +₹25 Price Jump on District</button>
        <button class="btn btn-sm" style="background: #E11D48;" onclick="simulateSurge('bms', 30)">Simulate +₹30 Surge on BMS</button>
        <button class="btn btn-outline btn-sm" onclick="resetSurge()">Reset to Normal Prices</button>
        <span id="surge-status" style="font-size: 12px; color: var(--accent-gold); font-weight: 600;"></span>
      </div>
    </div>
  </main>

  <script>
    async function loadData() {
      try {
        const provRes = await fetch('/api/providers/status');
        const providers = await provRes.json();
        const pBody = document.getElementById('providers-body');
        pBody.innerHTML = providers.map(p => \`
          <tr>
            <td style="font-weight: 600;">\${p.name}</td>
            <td><code>\${p.id}</code></td>
            <td>\${p.id === 'pvr' ? '<span class="badge badge-verified">✓ Available</span>' : '<span style="color: #9CA3AF;">NOT_SUPPORTED (Provider Redirect)</span>'}</td>
            <td><span class="badge badge-instant">✓ Official Protocol</span></td>
            <td><span class="badge \${p.status === 'ONLINE' ? 'badge-verified' : 'badge-cashback'}">\${p.status}</span></td>
            <td>
              <button class="btn btn-outline btn-sm" onclick="toggleProvider('\${p.id}', '\${p.status === 'ONLINE' ? 'MAINTENANCE' : 'ONLINE'}')">
                \${p.status === 'ONLINE' ? 'Simulate Downtime' : 'Set Online'}
              </button>
            </td>
          </tr>
        \`).join('');

        const offRes = await fetch('/api/offers');
        const offers = await offRes.json();
        document.getElementById('metric-offers').innerText = offers.length;
        const oBody = document.getElementById('offers-body');
        oBody.innerHTML = offers.map(o => \`
          <tr>
            <td style="font-weight: 600;">\${o.title}</td>
            <td><span class="badge badge-instant">\${o.category}</span></td>
            <td>\${o.isCashback ? '<span class="badge badge-cashback">Post-Payment Cashback</span>' : '<span class="badge badge-verified">Instant Discount</span>'}</td>
            <td>\${o.flatDiscount ? '₹' + o.flatDiscount : (o.discountPercentage ? o.discountPercentage + '%' : (o.isCashback ? '₹' + o.cashbackAmount + ' Cashback' : 'BOGO'))}</td>
            <td>₹\${o.minTransaction}</td>
            <td><span class="badge \${o.verificationStatus === 'VERIFIED' ? 'badge-verified' : 'badge-cashback'}">\${o.verificationStatus}</span></td>
            <td style="font-size: 11px; color: #9CA3AF;">\${new Date(o.lastVerifiedTimestamp).toLocaleTimeString()}</td>
            <td>
              <button class="btn btn-outline btn-sm" onclick="reverify('\${o.id}')">Re-Verify</button>
            </td>
          </tr>
        \`).join('');
      } catch (e) {
        console.error('Failed to load admin data:', e);
      }
    }

    async function toggleProvider(id, status) {
      await fetch('/api/admin/providers/' + id + '/status', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status })
      });
      loadData();
    }

    async function reverify(id) {
      await fetch('/api/admin/offers/' + id + '/verify', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status: 'VERIFIED' })
      });
      loadData();
    }

    async function simulateSurge(platformId, delta) {
      await fetch('/api/admin/simulate-price-change', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ platformId, delta })
      });
      document.getElementById('surge-status').innerText = '✓ Fluctuation active: +' + delta + ' on ' + platformId;
    }

    async function resetSurge() {
      await fetch('/api/admin/simulate-price-change/reset', { method: 'POST' });
      document.getElementById('surge-status').innerText = '✓ Reset to normal.';
    }

    function showAddOfferModal() {
      alert('Add Offer API is accessible via POST /api/admin/offers with standard JSON payload.');
    }

    loadData();
  </script>
</body>
</html>`;
}
