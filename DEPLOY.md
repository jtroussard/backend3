gcloud config set project devlife4me-generic-apps

gcloud config list --format="value(core.project)"

gcloud services enable \
run.googleapis.com \
cloudbuild.googleapis.com \
cloudresourcemanager.googleapis.com \
iam.googleapis.com \
secretmanager.googleapis.com \
sqladmin.googleapis.com \
artifactregistry.googleapis.com

gcloud services list --enabled

gcloud iam service-accounts create backend3-sql-access \
--description="Service account for Backend3 to access Cloud SQL" \
--display-name="Backend3 SQL Access"

gcloud iam service-accounts list --filter="backend3-sql-access"

gcloud projects add-iam-policy-binding devlife4me-generic-apps \
--member="serviceAccount:backend3-sql-access@devlife4me-generic-apps.iam.gserviceaccount.com" \
--role="roles/cloudsql.client"

gcloud projects add-iam-policy-binding devlife4me-generic-apps \
--member="serviceAccount:backend3-sql-access@devlife4me-generic-apps.iam.gserviceaccount.com" \
--role="roles/cloudsql.instanceUser"

gcloud projects add-iam-policy-binding devlife4me-generic-apps \
--member="serviceAccount:backend3-sql-access@devlife4me-generic-apps.iam.gserviceaccount.com" \
--role="roles/secretmanager.secretAccessor"

mm_sparrow@mac-mini backend3 % gcloud projects get-iam-policy devlife4me-generic-apps --format="json(bindings)" | grep backend3-sql-access

gcloud sql instances create backend3-postgresql-dev \
--database-version=POSTGRES_15 \
--tier=db-f1-micro \
--region=us-central1 \
--storage-type=SSD \
--activation-policy=ALWAYS

gcloud sql instances list --filter="backend3-postgresql-dev"

gcloud sql databases create backend3 --instance=backend3-postgresql-dev

gcloud sql users create db3_user --instance=backend3-postgresql-dev --password="mSwR5wPCdwcE3S9S5mVJR2cpEq15wLHEN5G7x3Ef"

gcloud sql databases list --instance=backend3-postgresql-dev

gcloud sql users list --instance=backend3-postgresql-dev

echo -n "YOUR_DEV_DB_PASSWORD" | gcloud secrets create backend3-dev-db-password \
--replication-policy="automatic" \
--data-file=-

echo -n "YOUR_PROD_DB_PASSWORD" | gcloud secrets create backend3-prod-db-password \
--replication-policy="automatic" \
--data-file=-

gcloud sql instances describe backend3-postgresql-dev --format="json(connectionName)"

echo -n "jdbc:postgresql:///backend3?cloudSqlInstance=devlife4me-generic-apps:us-central1:backend3-postgresql-dev&socketFactory=com.google.cloud.sql.postgres.SocketFactory" | \
gcloud secrets create backend3-dev-db-url --replication-policy="automatic" --data-file=-

echo -n "db3_user" | gcloud secrets create backend3-dev-db-username \
--replication-policy="automatic" \
--data-file=-

mm_sparrow@mac-mini backend3 % openssl rand -base64 32

echo -n "QRxfco8KgeAz2xhWD3Eyu0qTQ7b013pgyAQZ1TpoEFE=" | gcloud secrets create backend3-dev-jwt-secret \
--replication-policy="automatic" \
--data-file=-

MANUAL TASK: go to github and set a repo secret key/value for `GCP_PROJECT_ID` to equal the GCP project id

docker build -t backend3-app .

docker images | grep backend3-app

docker run --rm -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local backend3-app

gcloud artifacts repositories create backend3 \
--repository-format=docker \
--location=us-central1 \
--description="Docker repository for Backend3"

gcloud  artifacts repositories list --location=us-central1

gcloud auth configure-docker us-central1-docker.pkg.dev

cat ~/.docker/config.json | grep us-central1-docker.pkg.dev

docker tag backend3-app us-central1-docker.pkg.dev/devlife4me-generic-apps/backend3/backend3-app:latest

docker images | grep backend3-app

docker push us-central1-docker.pkg.dev/devlife4me-generic-apps/backend3/backend3-app:latest

gcloud artifacts docker images list us-central1-docker.pkg.dev/devlife4me-generic-apps/backend3

# PIN

mm_sparrow@mac-mini backend3 % gcloud run deploy backend3-service \
--image=us-central1-docker.pkg.dev/devlife4me-generic-apps/backend3/backend3-app:latest \
--platform=managed \
--region=us-central1 \
--allow-unauthenticated \
--set-env-vars "SPRING_PROFILES_ACTIVE=dev" \
--set-env-vars "GCP_PROJECT_ID=devlife4me-generic-apps" \
--service-account=backend3-sql-access@devlife4me-generic-apps.iam.gserviceaccount.com
Deploying container to Cloud Run service [backend3-service] in project [devlife4me-generic-apps] region [us-central1]
X Deploying new service...
- Creating Revision...                                                                                                                                                                                                                          
  . Routing traffic...                                                                                                                                                                                                                            
  ✓ Setting IAM Policy...                                                                                                                                                                                                                         
  Deployment failed                                                                                                                                                                                                                                 
  ERROR: (gcloud.run.deploy) Revision 'backend3-service-00001-hwt' is not ready and cannot serve traffic. The user-provided container failed to start and listen on the port defined provided by the PORT=8080 environment variable within the allocated timeout. This can happen when the container port is misconfigured or if the timeout is too short. The health check timeout can be extended. Logs for this revision might contain more information.

Logs URL: https://console.cloud.google.com/logs/viewer?project=devlife4me-generic-apps&resource=cloud_run_revision/service_name/backend3-service/revision_name/backend3-service-00001-hwt&advancedFilter=resource.type%3D%22cloud_run_revision%22%0Aresource.labels.service_name%3D%22backend3-service%22%0Aresource.labels.revision_name%3D%22backend3-service-00001-hwt%22
For more troubleshooting guidance, see https://cloud.google.com/run/docs/troubleshooting#container-failed-to-start
mm_sparrow@mac-mini backend3 % 






