-- V2__add_indexes.sql
-- Purpose: Add indexes to support search, filtering, and pagination
-- Indexes added:
--   - date (date range filtering)
--   - lorry_number (search)
--   - from_location (search)
--   - to_location (search)
--   - consignor_name (search)
-- Notes:
--   - Performance-only migration
--   - No schema changes

-- Date range filtering
CREATE INDEX IF NOT EXISTS idx_lorry_date ON lorry(date);

-- Search helpers
CREATE INDEX IF NOT EXISTS idx_lorry_lorry_number ON lorry(lorry_number);
CREATE INDEX IF NOT EXISTS idx_lorry_from_location ON lorry(from_location);
CREATE INDEX IF NOT EXISTS idx_lorry_to_location ON lorry(to_location);
CREATE INDEX IF NOT EXISTS idx_lorry_consignor_name ON lorry(consignor_name);