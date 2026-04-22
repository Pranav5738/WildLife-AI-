# Wildlife AI - Deployment Guide

## Prerequisites
- Docker installed
- Docker daemon running
- Maven 3.6+ (or use provided mvnw)

## Local Development

### 1. Build the Application
```bash
cd wildlife
./mvnw.cmd clean package -DskipTests
```

### 2. Build Docker Image
```bash
docker build -t wildlife-app:1.0 .
```

### 3. Run with Docker Compose
```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`

## Deployment to Render

### 1. Prerequisites
- GitHub account with this repository
- Render account (https://render.com)

### 2. Deploy via Render Dashboard
1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click "New +" and select "Web Service"
3. Connect your GitHub repository
4. Configure:
   - **Runtime**: Docker
   - **Build Command**: (auto-detected from Dockerfile)
   - **Start Command**: (auto-detected from Dockerfile)
   - **Environment Variables**:
     - `SPRING_PROFILES_ACTIVE=prod`
     - `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`
5. Click "Create Web Service"

### 3. Environment Variables on Render
Add the following in Render dashboard:

```
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false
DATABASE_URL=<your-database-url>
JWT_SECRET=<your-secret-key>
TWILIO_ACCOUNT_SID=<your-twilio-sid>
TWILIO_AUTH_TOKEN=<your-twilio-token>
AWS_ACCESS_KEY_ID=<your-aws-key>
AWS_SECRET_ACCESS_KEY=<your-aws-secret>
```

### 4. Health Checks
The application has a health check endpoint: `/api/health`
Render will monitor this to ensure your service is running.

## Production Optimizations Applied

✅ Multi-stage Docker build (smaller image size)
✅ Non-root user for security
✅ Health checks enabled
✅ JRE instead of JDK (reduced footprint)
✅ Alpine Linux base (lightweight)
✅ Proper labels and metadata
✅ .dockerignore configured

## Testing

### Test locally
```bash
curl http://localhost:8080/api/health
```

### View logs
```bash
docker-compose logs -f
```

### Stop containers
```bash
docker-compose down
```

## Troubleshooting

### Container won't start
- Check logs: `docker-compose logs wildlife-app`
- Verify environment variables are set
- Check database connectivity

### Health check failing
- Verify endpoint exists: `/api/health`
- Check application logs for errors
- Ensure Spring Boot is running on port 8080

### Large image size
- Already optimized with multi-stage build
- Using JRE instead of JDK
- Using Alpine Linux

## Configuration Files

- `Dockerfile` - Multi-stage production build
- `docker-compose.yml` - Local development setup
- `render.yaml` - Render deployment configuration
- `.dockerignore` - Excludes unnecessary files from build context

## First Deploy Checklist

- [ ] Push code to GitHub
- [ ] Create Render account
- [ ] Connect GitHub repository to Render
- [ ] Configure environment variables
- [ ] Set health check endpoint: `/api/health`
- [ ] Deploy and monitor logs
- [ ] Test API endpoints
